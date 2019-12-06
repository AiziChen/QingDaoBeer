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
import java.io.IOException;

/**
 * 抽奖线程
 *
 * @author Quanyec
 */
@Component
public class DrawRunnable implements Runnable {
    private static final long SEPARATE_TIME = 2 * 1000;
    private static final int TIMES = 3;
    private static final int PAGE_SIZE = 10;

    @Autowired
    private PhoneRepository phoneRepo;
    public static DrawRunnable clazz;

    @PostConstruct
    public void init() {
        DrawRunnable.clazz = this;
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
                    try {
                        Net_.launchHome();
                        drawLuck(p.getPhone());
                        Thread.sleep(SEPARATE_TIME);
                    } catch (InterruptedException | IOException e) {
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
    private void drawLuck(String phone) throws IOException {
        String code = Net_.getImageCode();
        if (code == null) {
            System.err.println("获取图片验证码出错~~~");
            return;
        }

        String hashStylePhoneNumber = Net_.validateImageCode(phone, code);
        for (int i = 0; hashStylePhoneNumber == null; i++) {
            if (i > 10) {
                return;
            }
            System.err.println(phone + " 验证码验证出错，正在重新获取...");
            try {
                // Sleep 1 second
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            // 重新获取验证码
            code = Net_.getImageCode();
            // 重新验证
            hashStylePhoneNumber = Net_.validateImageCode(phone, code);
        }

        String result = Net_.startDraw(hashStylePhoneNumber, code);
        System.out.print("手机号: `" + phone + "`");
        if (result != null) {
            JSONObject resJson = JSON.parseObject(result);
            int status = resJson.getInteger("status");
            switch (status) {
                case 000:
                case 200:
                    System.out.println(", 成功抽奖。");
                    break;
                case 500:
                    System.err.println("没抽奖次数了哦，改日再战吧!");
                    break;
                case 400:
                    System.err.println("当前参与人数众多，请稍后再试！");
                    break;
                case 700:
                    System.err.println("当前抽奖人数过多，请稍后重试！");
                    break;
                default:
                    System.out.println(", 抽奖失败。");
            }
        } else {
            System.err.println("，抽奖失败。");
        }
    }
}
