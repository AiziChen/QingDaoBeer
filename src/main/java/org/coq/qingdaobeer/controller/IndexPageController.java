package org.coq.qingdaobeer.controller;

import org.coq.qingdaobeer.entity.Msg;
import org.coq.qingdaobeer.tools.Image_;
import org.coq.qingdaobeer.tools.Net_;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Using to launch `index.ftl` page
 *
 * @author Quanyec
 */
@Controller
public class IndexPageController {

    public static TreeMap<String, String> codes = new TreeMap<>();

    @RequestMapping("/")
    public String launchPage() {
        return "index";
    }


    @RequestMapping("/getCode")
        @ResponseBody
        public Msg getCode() {
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
        if (codes.size() >= 999) {
            codes.clear();
        }
        codes.put(request.getRemoteHost(), code);
        try {
            response.setContentType("image/png");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            Image_.outputImage(100, 36, response.getOutputStream(), code);
        } catch (Exception ignored) {
        }
    }
}
