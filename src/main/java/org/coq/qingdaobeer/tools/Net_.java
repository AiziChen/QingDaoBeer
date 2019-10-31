package org.coq.qingdaobeer.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Network Tool
 */
public class Net_ {
    private static final String USER_ID = "63E014187E0DAE7818EE2AAB125B4269";
    private static CloseableHttpClient client;

    /**
     * Start Snatch
     *
     * @param phone
     * @param code
     * @return
     */
    public static String startSnatch(String phone, String code) {
        String url = "https://m.client.10010.com/sma-lottery/qpactivity/qpLuckdraw.htm";
        HttpPost poster = new HttpPost(url);
        poster.addHeader("Referer", "https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex");
        poster.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
        Map<String, String> values = new HashMap<>();
        values.put("mobile", phone);
        values.put("image", code);
        values.put("userid", USER_ID);
        try {
            UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(getParam(values), "UTF-8");
            poster.setEntity(encodedFormEntity);
            // do request
            HttpResponse resp = client.execute(poster);
            if (resp.getStatusLine().getStatusCode() == 200) {
                String responseText = EntityUtils.toString(resp.getEntity());
                return responseText;
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return null;
    }


    private static List<NameValuePair> getParam(Map parameterMap) {
        List<NameValuePair> param = new ArrayList<>();
        for (Object o : parameterMap.entrySet()) {
            Map.Entry parmEntry = (Map.Entry) o;
            param.add(new BasicNameValuePair((String) parmEntry.getKey(),
                    (String) parmEntry.getValue()));
        }
        return param;
    }


    /**
     * Connect to link to get the image-code
     *
     * @param phone
     * @return `null` if get failed, or the `code` if get success
     */
    public static String getImageCode(String phone) {
        client = HttpClientBuilder.create().build();
        String url = "https://m.client.10010.com/sma-lottery/qpactivity/getSysManageLoginCode.htm";
        url = url + "?userid=" + USER_ID + "&code=" + System.currentTimeMillis();
        HttpGet getter = new HttpGet(url);
        getter.addHeader("Referer", "https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex");
        getter.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
        try {
            HttpResponse response = client.execute(getter);
            if (response.getStatusLine().getStatusCode() == 200) {
                byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                C_.downloadImage(bytes, phone + ".png");
                return C_.getImgContent(phone + ".png");
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }


    /**
     * Get hash-style mobile
     *
     * @param phone
     * @param code
     * @return hash-style mobile-format
     */
    public static String validateImageCode(String phone, String code) {
        String url = "https://m.client.10010.com/sma-lottery/validation/qpImgValidation.htm";
        HttpPost poster = new HttpPost(url);
        poster.addHeader("Referer", "https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex");
        poster.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
        Map<String, String> values = new HashMap<>();
        values.put("mobile", phone);
        values.put("image", code);
        values.put("userid", USER_ID);
        try {
            UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(getParam(values), "UTF-8");
            poster.setEntity(encodedFormEntity);
            // do request
            HttpResponse resp = client.execute(poster);
            if (resp.getStatusLine().getStatusCode() == 200) {
                String responseText = EntityUtils.toString(resp.getEntity());
                JSONObject resJson = JSON.parseObject(responseText);
                if (resJson != null && resJson.getString("code").equals("YES")) {
                    return resJson.getString("mobile");
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return null;
    }

}
