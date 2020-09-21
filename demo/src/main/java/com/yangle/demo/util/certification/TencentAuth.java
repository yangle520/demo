package com.yangle.demo.util.certification;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import okhttp3.Response;

public class TencentAuth {

    private static final String appId  = "TIDAMkSY";
    private static final String secret = "hbkAOZ5oRApffJtn11b8kBQe3DIwYgfWGt0M8BO6O1sWz8gKPycJPQXnI6jVfwnq";

    private static final String TICKET_TYPE_NONCE = "NONCE";
    private static final String TICKET_TYPE_SIGN  = "SIGN";

    private static String accessToken = "";
    private static String signTicket  = "";

    /**
     * 获取服务端ticket
     *
     * @return
     */
    public static String getSignTicket() {
        return getTicket(TICKET_TYPE_SIGN, null);
    }

    /**
     * 获取前端ticket
     *
     * @param userId
     * @return
     */
    public static String getNonceTicket(String userId) {
        return getTicket(TICKET_TYPE_NONCE, userId);
    }

    private static String getAccessToken() {
        if (!Strings.isNullOrEmpty(accessToken)) {
            // 从缓存中取
            return accessToken;
        }
        try {
            String url = "https://idasc.webank.com/api/oauth2/access_token?grant_type=client_credential&version=1.0.0&app_id=" + appId + "&secret=" + secret;
            Response response = TencentHttp.get(url);
            String result = response.body().string();

            System.out.println("accessToken:" + result);

            JSONObject res = JSONObject.parseObject(result);
            if ("0".equals(res.getString("code"))) {
                accessToken = res.getString("access_token");
                return res.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getTicket(String type, String userId) {
        String accessToken = getAccessToken();
        if (TICKET_TYPE_SIGN.equals(type) && !Strings.isNullOrEmpty(signTicket)) {
            // 从缓存中取
            return signTicket;
        }

        try {
            String url = "https://idasc.webank.com/api/oauth2/api_ticket?app_id=" + appId + "&type=" + type + "&version=1.0.0&access_token=" + accessToken;
            if (TICKET_TYPE_NONCE.equals(type)) {
                url = url + "&user_id=" + userId;
            }

            Response response = TencentHttp.get(url);
            String result = response.body().string();
            System.out.println(type + "Ticket:" + result);

            JSONObject res = JSONObject.parseObject(result);
            if ("0".equals(res.getString("code"))) {
                if (TICKET_TYPE_SIGN.equals(type)) {
                    signTicket = res.getJSONArray("tickets").getJSONObject(0).getString("value");
                }
                return res.getJSONArray("tickets").getJSONObject(0).getString("value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
