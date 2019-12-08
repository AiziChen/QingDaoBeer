package org.coq.qingdaobeer.controller;

import org.coq.qingdaobeer.entity.Msg;
import org.coq.qingdaobeer.tools.Image_;
import org.coq.qingdaobeer.tools.Net_;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Using to launch `index.ftl` page
 *
 * @author Quanyec
 */
@Controller
public class IndexPageController {

    public static ConcurrentHashMap<String, String> codes = new ConcurrentHashMap<>();

    @RequestMapping("/")
    public String launchPage() {
        return "index";
    }


    @RequestMapping("/getCode")
    @ResponseBody
    public Msg getCode() {
        Net_.launchHome();
        String code = Net_.getImageCode();
        if (code == null) {
            return new Msg(-1, "获取验证码出错");
        } else {
            return new Msg(1, code);
        }
    }

    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        String code = Image_.getRandomCode(4);
        if (codes.size() >= 20) {
            codes.clear();
        }
        codes.put(request.getRemoteHost(), code);
        try {
            response.setContentType("image/png");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            Image_.outputImage(110, 36, response.getOutputStream(), code);
        } catch (Exception ignored) {
        }
    }
}
