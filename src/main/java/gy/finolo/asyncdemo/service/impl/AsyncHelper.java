package gy.finolo.asyncdemo.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @description: 异步辅助类
 * @author: Simon
 * @date: 2020-06-16 09:20
 */
@Component
public class AsyncHelper {

    @Async
    public Future<String> asyncReturn(int i) {

       /* if (i == 0) {
            int j = 1 / 0;
        }*/
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
//            e.printStackTrace();
            return null;
        }

        return new AsyncResult<>("async with return: " + i);
    }
}
