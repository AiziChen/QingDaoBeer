package org.coq.qingdaobeer.entity;

/**
 * Message object
 *
 * @author Quanyec
 */
public class Msg {
    private Integer status;
    private String msg;

    public Msg() {
    }

    public Msg(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
