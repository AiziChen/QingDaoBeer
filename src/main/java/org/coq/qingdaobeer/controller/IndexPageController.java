package org.coq.qingdaobeer.controller;

import org.coq.qingdaobeer.entity.Msg;
import org.coq.qingdaobeer.tools.Net_;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

/**
 * Using to launch `index.ftl` page
 *
 * @author Quanyec
 */
@Controller
public class IndexPageController {
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
}
