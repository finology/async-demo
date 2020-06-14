package gy.finolo.asyncdemo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @description: 同步服务
 * @author: Simon
 * @date: 2020-06-14 22:49
 */
@Service
public class AsyncService {

    public void syncNoReturn() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("syncService.syncNoReturn ends");
    }

    @Async
    public void asyncNoReturn() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("asyncService.asyncNoReturn ends");
    }
}
