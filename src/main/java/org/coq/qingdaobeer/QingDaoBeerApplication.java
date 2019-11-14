package org.coq.qingdaobeer;

import org.coq.qingdaobeer.thread.DrawRunable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        Thread snatchThread = new Thread(new DrawRunable());
        snatchThread.start();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                // 00:10:00 时刻开抢
                System.out.println("当前时间：" + sdf.format(now));
                if (now.getHours() == 0 && now.getMinutes() == 10) {
                    Thread snatchThread = new Thread(new DrawRunable());
                    snatchThread.start();
                }
            }
        }, 1000, 58 * 1000);
    }

}
