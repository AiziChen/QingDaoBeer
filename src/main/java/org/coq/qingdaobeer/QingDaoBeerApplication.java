package org.coq.qingdaobeer;

import org.coq.qingdaobeer.thread.DrawRunnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SpringBooter main
 *
 * @author Quanyec
 */
@SpringBootApplication
public class QingDaoBeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingDaoBeerApplication.class, args);
//         立即开启线程可实时测试
//        Thread snatchThread = new Thread(new DrawRunnable());
//        snatchThread.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                long cs = System.currentTimeMillis() / 1000;
                long cm = cs / 60;
                long ch = cm / 60;
//                long second = cs % 60;
                long minute = cm % 60;
                long hour = (8 + ch % 24);
                // 00:06:00 时刻开抢
                if (hour == 0 && minute == 10) {
                    Thread snatchThread = new Thread(new DrawRunnable());
                    snatchThread.start();
                }
            }
        }, 1000, 58 * 1000);
    }

}
