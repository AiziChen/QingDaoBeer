package org.coq.qingdaobeer.controller;

import org.coq.qingdaobeer.entity.Msg;
import org.coq.qingdaobeer.record.Phone;
import org.coq.qingdaobeer.repo.PhoneRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    private final PhoneRepository phoneRepo;

    public PhoneController(PhoneRepository phoneRepo) {
        this.phoneRepo = phoneRepo;
    }

    @PostMapping("/pushPhone")
    public Msg pushNewPhone(@RequestParam String phone, @RequestParam String code, HttpServletRequest request) {
        if (phone.length() != 11) {
            return new Msg(-1, "输入的手机号不正确。");
        }
        String host = request.getRemoteHost();
        if (!IndexPageController.codes.get(host).equals(code)) {
            return new Msg(-1, "验证码不正确");
        }
        try {
            phoneRepo.save(new Phone(phone, new Date()));
        } catch (Exception e) {
            return new Msg(-1, "手机号: " + phone + "重复添加!");
        }
        return new Msg(1, "添加挂机成功");
    }


    @RequestMapping("/rmPhone")
    public Msg deletePhone(@RequestParam String phone) {
        if (phone.length() != 11) {
            return new Msg(-1, "输入的手机号不正确。");
        }
        Phone p = phoneRepo.findByPhoneNumber(phone);
        if (p != null) {
            phoneRepo.delete(p);
            return new Msg(1, "号码删除成功");
        } else {
            return new Msg(-1, "没有找到该号码。删除失败。");
        }
    }
}
