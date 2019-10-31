package org.coq.qingdaobeer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
