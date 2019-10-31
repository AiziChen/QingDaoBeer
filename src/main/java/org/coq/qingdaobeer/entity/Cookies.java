package org.coq.qingdaobeer.entity;

import org.apache.http.client.CookieStore;

/**
 * Cookies
 *
 * @author Quanyec
 */
public class Cookies {
    private String cookies;
    private String jSessionId;

    public Cookies() {
    }

    public Cookies(String cookies, String jSessionId) {
        this.cookies = cookies;
        this.jSessionId = jSessionId;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getjSessionId() {
        return jSessionId;
    }

    public void setjSessionId(String jSessionId) {
        this.jSessionId = jSessionId;
    }
}
