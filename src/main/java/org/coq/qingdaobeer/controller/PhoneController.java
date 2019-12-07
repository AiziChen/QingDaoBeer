package org.coq.qingdaobeer.controller;

import org.coq.qingdaobeer.entity.Msg;
import org.coq.qingdaobeer.record.Phone;
import org.coq.qingdaobeer.repo.PhoneRepository;
import org.coq.qingdaobeer.tools.Net_;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    private final PhoneRepository phoneRepo;

    public PhoneController(PhoneRepository phoneRepo) {
        this.phoneRepo = phoneRepo;
    }

    @PostMapping("/pushPhone")
    public Msg pushNewPhone(@NotNull @RequestParam String phone, @NotNull @RequestParam String code, HttpServletRequest request) {
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return new Msg(-1, "输入的手机号不正确");
        }
        String host = Net_.getIpAddr(request);
        if (host == null) {
            return new Msg(-1, "请重新申请验证码");
        }
        if (!IndexPageController.codes.get(host).toLowerCase()
                .equals(code.toLowerCase())) {
            return new Msg(-1, "验证码不正确");
        }
        try {
            phoneRepo.save(new Phone(phone, new Date()));
        } catch (Exception e) {
            return new Msg(-1, "手机号: " + phone + "重复添加!");
        }
        IndexPageController.codes.remove(host);
        return new Msg(1, "添加挂机成功");
    }


    @RequestMapping("/rmPhone")
    public Msg deletePhone(@NotNull @RequestParam String phone, @NotNull @RequestParam String code, HttpServletRequest request) {
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return new Msg(-1, "输入的手机号不正确");
        }
        String host = Net_.getIpAddr(request);
        if (host == null) {
            return new Msg(-1, "请重新申请验证码");
        }
        if (!IndexPageController.codes.get(host).toLowerCase()
                .equals(code.toLowerCase())) {
            return new Msg(-1, "验证码不正确");
        }
        IndexPageController.codes.remove(host);
        Phone p = phoneRepo.findByPhoneNumber(phone);
        if (p != null) {
            phoneRepo.delete(p);
            return new Msg(1, "号码取消挂机成功");
        } else {
            return new Msg(1, "该号码未在挂机列表");
        }
    }
}
