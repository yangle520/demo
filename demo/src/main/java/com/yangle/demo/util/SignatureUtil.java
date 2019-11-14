package com.yangle.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

public class SignatureUtil {

    private static final String accessKeyId = "xxxxxx";
    private static final String secret      = "xxxxxxxxxx";

    public static void main(String[] args) throws Exception {

        // 请求参数 -- 包含 公共参数 和 接口参数
        JSONObject params = new JSONObject();
        params.put("Action", "aaaa");
        params.put("Region", "bbb");

        String endpoint = "https://api.unicloud.com/instance?";

        // 生成http请求url
        String url = endpoint + getUrl(params, accessKeyId, secret);
        System.out.println(url);

    }

    public static String getUrl(JSONObject json, String ak, String aks) throws Exception {

        // 将AK加入请求参数
        json.put("AccessKeyId", ak);

        // 将原始uri 中的key value 进行 encode
        List<String> params = json.keySet().stream().map(key -> encode(key) + "=" + encode(json.getString(key))).collect(Collectors.toList());

        // 排序参数
        List<String> pList = params.stream().map(SignatureUtil::encode).sorted().collect(Collectors.toList());

        // 将转译后的请求参数用&拼接   & encode 之后 是 %26
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
        return url;
    }

    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {

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
        try {
            return s != null ? URLEncoder.encode(s, "UTF-8").replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~") : null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
