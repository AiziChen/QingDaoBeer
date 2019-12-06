package org.coq.qingdaobeer;

import org.coq.qingdaobeer.thread.DrawRunnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SpringBooter main
 *
 * @author Quanyec
 */
@SpringBootApplication
public class QingDaoBeerApplication {

    private static final Calendar c = Calendar.getInstance(Locale.CHINA);

    public static void main(String[] args) {
        SpringApplication.run(QingDaoBeerApplication.class, args);
//         立即开启线程可实时测试
        Thread snatchThread = new Thread(new DrawRunnable());
        snatchThread.start();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                System.out.println("当前时间：" + sdf.format(c.getTime()));
                // 00:10:00 时刻开抢
                if (c.get(Calendar.HOUR_OF_DAY) == 0 && c.get(Calendar.MINUTE) == 10) {
                    Thread snatchThread = new Thread(new DrawRunnable());
                    snatchThread.start();
                }
            }
        }, 1000, 58 * 1000);
    }

}
