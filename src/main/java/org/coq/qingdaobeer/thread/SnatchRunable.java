package org.coq.qingdaobeer.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.coq.qingdaobeer.record.Phone;
import org.coq.qingdaobeer.repo.PhoneRepository;
import org.coq.qingdaobeer.tools.Net_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 抽奖线程
 *
 * @author Quanyec
 */
@Component
public class SnatchRunable implements Runnable {
    private static final long SEPARATE_TIME = 2 * 1000;
    private static final int TIMES = 3;
    private static final int PAGE_SIZE = 10;

    @Autowired
    private PhoneRepository phoneRepo;
    public static SnatchRunable clazz;

    @PostConstruct
    public void init() {
        SnatchRunable.clazz = this;
        clazz.phoneRepo = this.phoneRepo;
    }

    @Override
    public void run() {
        for (int j = 0; j < TIMES; ++j) {
            for (int i = 0; ; ++i) {
                Pageable pageable = PageRequest.of(i, PAGE_SIZE);
                Page<Phone> pages = clazz.phoneRepo.findByPage(pageable);
                if (pages.isEmpty()) {
                    break;
                }
                for (Phone p : pages) {
                    doSnatch(p.getPhone());
                    try {
                        Thread.sleep(SEPARATE_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 执行抽奖流程
     *
     * @param phone
     */
    private void doSnatch(String phone) {
        String code = Net_.getImageCode(phone);
        if (code == null) {
            System.err.println(phone + "获取图片验证码出错。");
            return;
        }

        String hashStylePhoneNumber = Net_.validateImageCode(phone, code);
        while (hashStylePhoneNumber == null) {
            System.err.println(phone + "验证码验证出错。");
            // 重新获取验证码
            hashStylePhoneNumber = Net_.validateImageCode(phone, code);
        }

        String result = Net_.startSnatch(hashStylePhoneNumber, code);
        if (result != null) {
            JSONObject resJson = JSON.parseObject(result);
            int status = resJson.getInteger("status");
            switch (status) {
                case 200:
                    System.out.println(phone + ", 成功抽奖。");
                    break;
                default:
                    System.out.println(phone + ", 抽奖失败。");
            }
        } else {
            System.err.println(phone + "，抽奖失败。");
        }
    }
}
