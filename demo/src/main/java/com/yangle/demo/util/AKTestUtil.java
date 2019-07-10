package com.yangle.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

public class AKTestUtil {

    private static final String NORMAL_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.~";
    private static final String accessKeyId = "UroJTwfvekDfuBZf";
    private static final String secret      = "uDWffMfOn72ObqJWmjHfxtI2EK4D7f";


    public static String getUrl(JSONObject json, String ak, String aks) throws Exception {

        // 原始请求参数
        json.put("AccessKeyId", ak);

        // 将原始uri 中的key value 进行 encode
        List<String> params = json.keySet().stream().map(key -> key + "=" + encode(json.getString(key))).collect(Collectors.toList());
        List<String> pList = params.stream().map(AKTestUtil::encode).sorted().collect(Collectors.toList());

        // 将转译后的请求参数用&拼接  %26 == &
        String canonicalizedQueryString = Joiner.on("%26").join(pList);
        System.out.println(canonicalizedQueryString);

        // 生成 stringToSign
        String stringToSign = Joiner.on("&").join("GET", URLEncoder.encode("/", "utf-8"), canonicalizedQueryString);
        System.out.println(stringToSign);

        // 生成 hmac
        String hmac = HmacSHA1Encrypt(stringToSign, aks + "&");
        System.out.println(hmac);

        // 最终发出的url  需要对value 进行转译
        String url = Joiner.on("&").join(params) + "&Signature=" + encode(hmac);
        System.out.println(url);
        return url;
    }


    private static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {

        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(), "HmacSHA1");

        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");

        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        //完成 Mac 操作
        byte[] rawHmac = mac.doFinal(encryptText.getBytes());

        return new String(Base64.encodeBase64(rawHmac));
    }

    private static String encode(String s) {
        List<String> list = Lists.newArrayList();
        String[] v = s.split("");
        for (String a : v) {
            try {
                if (a.equals(" ")) {
                    list.add("%20");
                } else if (a.equals("*")) {
                    list.add("%2A");
                } else if (!NORMAL_CHAR.contains(a)) {
                    list.add(URLEncoder.encode(a, "UTF-8"));
                } else {
                    list.add(a);
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        return Joiner.on("").join(list);
    }

    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        System.out.println(encode("\""));
    }
}
