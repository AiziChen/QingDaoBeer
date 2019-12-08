package org.coq.qingdaobeer.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Network Tool
 *
 * @Quanyec
 */
public class Net_ {
    private static String USER_ID = "2A467CFE305D1CF6DF3A9B6202023EFF";
    private static OkHttpClient client = new OkHttpClient();
    private static HttpUrl httpUrl;

    /**
     * Get the HttpClient
     *
     * @return
     */
    public static OkHttpClient getHttpClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    /**
     * Start Draw
     *
     * @param phone
     * @param code
     * @return
     */
    public static String startDraw(String phone, String code) throws IOException {
        String url = "https://m.client.10010.com/sma-lottery/qpactivity/qpLuckdraw.htm";
        FormBody body = new FormBody.Builder()
                .add("mobile", phone)
                .add("image", code)
                .add("userid", USER_ID)
                .build();
        List<Cookie> cookies = getHttpClient().cookieJar().loadForRequest(httpUrl);
        StringBuilder csb = new StringBuilder();
        for (Cookie cookie : cookies) {
            csb.append(cookie.name()).append("=").append(cookie.value()).append(";");
        }
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Referer", "https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex")
                .addHeader("Cookie", csb.toString())
                .build();
        try (Response response = getHttpClient().newCall(request).execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        }
        return null;
    }


    /**
     * 加载主页，获取Cookies
     */
    public static void launchHome() {
        String url = "https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = getHttpClient().newCall(request).execute()) {
            if (response.body() != null) {
                Headers headers = response.headers();
                httpUrl = request.url();
                List<Cookie> cookies = Cookie.parseAll(httpUrl, headers);
                for (Cookie c : cookies) {
                    if (c.name().equals("JSESSIONID")) {
                        USER_ID = c.value();
                    }
                }
                client.cookieJar().saveFromResponse(httpUrl, cookies);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get image-inputStream
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream getImageStream(String url) {
        List<Cookie> cookies = getHttpClient().cookieJar().loadForRequest(httpUrl);
        StringBuilder csb = new StringBuilder();
        for (Cookie cookie : cookies) {
            csb.append(cookie.name()).append("=").append(cookie.value()).append(";");
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                .addHeader("Cookie", csb.toString())
                .build();
        try {
            Response response = getHttpClient().newCall(request).execute();
            if (response.body() != null) {
                return response.body().byteStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getImageStream(url);
    }


    /**
     * Connect to link to get the image-code
     *
     * @return `null` if get failed, or the `code` if get success
     */
    public static String getImageCode() {
        String url = "https://m.client.10010.com/sma-lottery/qpactivity/getSysManageLoginCode.htm";
        url = url + "?userid=" + USER_ID + "&code=" + System.currentTimeMillis();
        String code = C_.getImgContent(getImageStream(url));
        for (int i = 0; i < 10 && code == null; i++) {
            try {
                // Sleep 1 second
                Thread.sleep(1000);
                code = C_.getImgContent(getImageStream(url));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return code;
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
        FormBody body = new FormBody.Builder()
                .add("mobile", phone)
                .add("image", code)
                .add("userid", USER_ID)
                .build();
        List<Cookie> cookies = getHttpClient().cookieJar().loadForRequest(httpUrl);
        StringBuilder csb = new StringBuilder();
        for (Cookie cookie : cookies) {
            csb.append(cookie.name()).append("=").append(cookie.value()).append(";");
        }
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Referer", "https://m.client.10010.com/sma-lottery/qpactivity/qingpiindex")
                .addHeader("Cookie", csb.toString())
                .build();
        try (Response response = getHttpClient().newCall(request).execute()) {
            if (response.body() != null) {
                String content = response.body().string();
                JSONObject resJson = JSON.parseObject(content);
                if (resJson != null && resJson.getString("code").equals("YES")) {
                    return resJson.getString("mobile");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据HttpServletRequest 获取客户端真实ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断传入的手机号是否为联通运营商的手机号
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isUnicom(String phoneNumber) {
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + phoneNumber;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = getHttpClient().newCall(request).execute()) {
            ResponseBody respBody = response.body();
            if (respBody != null) {
                return respBody.string().contains("联通");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isUnicom(phoneNumber);
    }
}
