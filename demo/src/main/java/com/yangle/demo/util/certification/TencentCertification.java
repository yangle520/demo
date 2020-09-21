package com.yangle.demo.util.certification;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import okhttp3.Response;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TencentCertification {

    private static final String appId   = "TIDAMkSY";
    private static final String version = "1.0.0";

    // 通过照片获取姓名及身份证号
    public static JSONObject getIdCareInfo(String cardType, MultipartFile file) {
        String nonce = getNonce();
        String orderNo = buildOrderNo();
        try {
            String url = "https://ida.webank.com/api/paas/idcardocrapp";

            JSONObject params = new JSONObject();
            params.put("webankAppId", appId);
            params.put("version", version);
            params.put("nonce", nonce);
            params.put("sign", sign(Lists.newArrayList(appId, orderNo, nonce, version), TencentAuth.getSignTicket()));
            params.put("orderNo", orderNo);
            params.put("cardType", cardType);
            params.put("idcardStr", fileToBase64(file));

            Response response = TencentHttp.post(url, params.toJSONString());

            String result = response.body().string();
            System.out.println("getIdCareInfo:" + result);

            return JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }


    //验证身份证及姓名，并获得faceId
    public static String getH5faceId(String orderNo, String userId, String name, String idNo) {
        try {
            String url = "https://idasc.webank.com/api/server/h5/geth5faceid";

            JSONObject params = new JSONObject();
            params.put("webankAppId", appId);
            params.put("orderNo", orderNo);
            params.put("name", name);
            params.put("idNo", idNo);
            params.put("userId", userId);
            params.put("version", version);
            params.put("sign", sign(Lists.newArrayList(appId, orderNo, name, idNo, userId, version), TencentAuth.getSignTicket()));


            Response response = TencentHttp.post(url, params.toJSONString());

            String result = response.body().string();
            System.out.println("getH5faceId:" + result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 获取H5人脸识别url
    public static String init(String orderNo, String userId, String faceId) {
        String h5faceId = faceId;
        String nonce = getNonce();
        String sign = sign(Lists.newArrayList(appId, userId, nonce, version, h5faceId, orderNo), TencentAuth.getNonceTicket(userId));

        String url = "https://ida.webank.com/api/web/login?webankAppId=TIDAMkSY&version=1.0.0&nonce=" + nonce +
                "&orderNo=" + orderNo + "&h5faceId=" + faceId + "&url=https%3a%2f%2f60.28.77.18%3a12011%2fapi%2fdoc%2fres&resultType=0&userId=" + userId +
                "&sign=" + sign + "&from=browser&redirectType=1";
        return url;
    }

    // 后端查询核验结果
    public static JSONObject queryIdascRes(String orderNo) {
        String nonce = getNonce();
        String sign = sign(Lists.newArrayList(appId, orderNo, version, nonce), TencentAuth.getSignTicket());

        String url = "https://idasc.webank.com/api/server/sync?app_id=" + appId + "&version=1.0.0&nonce=" + nonce +
                "&order_no=" + orderNo + "&sign=" + sign + "&get_file=0";

        try {
            Response response = TencentHttp.get(url);

            JSONObject result = JSONObject.parseObject(response.body().string());
            System.out.println("idascRes:" + result.toJSONString());
            if (result != null && "0".equals(result.getString("code"))) {
                //base64ToFile(result.getJSONObject("result").getString("photo"), "/" + System.currentTimeMillis() + ".jpg");
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }


    private static String sign(List<String> values, String ticket) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }
        values.removeAll(Collections.singleton(null));// remove null
        values.add(ticket);
        java.util.Collections.sort(values);

        StringBuilder sb = new StringBuilder();
        for (String s : values) {
            sb.append(s);
        }
        return Hashing.sha1().hashString(sb, Charsets.UTF_8).toString().toUpperCase();
    }

    /**
     * 随机数：32位随机串
     *
     * @return
     */
    private static String getNonce() {
        return "nonce" + String.format("%024d", System.currentTimeMillis()) + (new Random().nextInt(900) + 100);
    }

    /**
     * 与第三方通讯流水号
     *
     * @return
     */
    private static String buildOrderNo() {
        return "order" + String.format("%024d", System.currentTimeMillis()) + (new Random().nextInt(900) + 100);
    }

    public static void main(String[] args) {
        System.out.println(getNonce());
        System.out.println(buildOrderNo());
    }

    /**
     * Base64 转 图片
     *
     * @param str
     * @param filePath
     * @return
     */
    public static boolean base64ToFile(String str, String filePath) {
        if (str == null) {
            return false;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(str);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    //调整异常数据
                    b[i] += 256;
                }
            }
            //新生成的图片
            OutputStream out = new FileOutputStream(filePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 图片 转 base64
     *
     * @return
     */
    public static String fileToBase64(MultipartFile file) throws IOException {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(file.getBytes());
    }


}
