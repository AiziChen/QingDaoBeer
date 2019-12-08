package org.coq.qingdaobeer.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.coq.qingdaobeer.file.LogFile;
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
import java.util.Calendar;
import java.util.Locale;

/**
 * 抽奖线程
 *
 * @author Quanyec
 */
@Component
public class DrawRunnable implements Runnable {
    private static final long SEPARATE_TIME = 15 * 100; // 1.5 s
    private static final int TIMES = 1;
    private static final int PAGE_SIZE = 10;

    private LogFile logFile;

    @Autowired
    private PhoneRepository phoneRepo;
    public static DrawRunnable clazz;

    @PostConstruct
    public void init() {
        DrawRunnable.clazz = this;
        clazz.phoneRepo = this.phoneRepo;
        initLogFile();
    }

    private void initLogFile() {
        try {
            logFile = new LogFile("qing-dao-beer.log", "rw");
            logFile.seek(logFile.length());
            logFile.write("=== ".getBytes());
            logFile.write(Calendar.getInstance(Locale.CHINA).getTime().toString().getBytes());
            logFile.write(" ===\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if (result != null) {
            JSONObject resJson = JSON.parseObject(result);
            String status = resJson.getString("status");
            switch (status) {
                case "000":
                case "200":
                    switch (resJson.getJSONObject("data").getInteger("level")) {
                        case 1:
                            logFile.appendSuccess(phone, "获得50M流量");
                            break;
                        case 2:
                            logFile.appendSuccess(phone, "获得100M流量");
                            break;
                        case 3:
                            logFile.appendSuccess(phone, "获得幸运奖");
                            break;
                        case 4:
                            logFile.appendSuccess(phone, "获得1000M流量");
                            break;
                        case 5:
                            logFile.appendSuccess(phone, "获得20钻石");
                            break;
                        case 6:
                            logFile.appendSuccess(phone, "获得20元");
                            break;
                        case 7:
                            logFile.appendSuccess(phone, "获得50元");
                            break;
                    }
                    break;
                case "500":
                    logFile.appendFailed(phone, "没抽奖次数了哦，改日再战吧！");
                    break;
                case "400":
                    logFile.appendFailed(phone, "当前参与人数众多，请稍后再试！");
                    break;
                case "700":
                    logFile.appendFailed(phone, "当前抽奖人数过多，请稍后重试！");
                    break;
            }
        } else {
            // 网络出错，重新抽奖
            drawLuck(phone);
        }
    }
}
