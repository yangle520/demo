package com.yangle.demo.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HTTPReqTest {


    public static void main(String[] args) throws Exception {

        String url = "https://api.unicloud.com/instance?CpuSku=UNIS_PC_GDGZ_A_CLOUDSERVER_MEMORY_CPU_MEM-1CORE-8GB&Action=RunInstances&SecurityGroupName=default&BandWidthSku=&BaseQuantity=1&PayType=YEAR_MONTH&PrivateNetId=default&Timestamp=2019-09-20T08%3A31%3A22Z&InstanceName=i-Kw3De8dGKF&SignatureVersion=1.0&Format=JSON&SignatureNonce=47554ddc-0c97-465f-a81c-5a66c8b5782c&DataDiskSku=&Version=2019-06-30&AccessKeyId=biWeZHT4qJQxFQxB&AuthValue=&ImageId=0e64fe17-8568-4e94-ba09-be0ecb18cbef&SysDiskSku=UNIS_PC_GDGZ_A_SYSDISK_NORMAL-40GB&SignatureMethod=HMAC-SHA1&RegionId=3efac1df-ac1b-47dc-a88f-b6649c5f8a1d&AuthType=auto&HostName=testzgyyyyy&Signature=erlTZMFoWfLsivKrEsVUWfQO1Nw%3D";
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(result);
    }

}
