package gy.finolo.asyncdemo.service.impl;

import gy.finolo.asyncdemo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @description: 同步服务
 * @author: Simon
 * @date: 2020-06-14 22:49
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private AsyncHelper asyncHelper;


    @Override
    public void syncNoReturn() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("syncService.syncNoReturn ends");
    }

    @Async
    @Override
    public void asyncNoReturn() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("asyncService.asyncNoReturn ends");
    }

    @Async
    @Override
    public Future<String> asyncReturn(int i) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new AsyncResult<>("async with return: " + i);
    }

    @Override
    public Map<String, Object> asyncReturn2() {

        long start = System.currentTimeMillis();
        List<Future<String>> futures = new ArrayList();

        for (int i = 0; i < 4; i++) {
//            Future<String> future = asyncService.asyncReturn(i);
            Future<String> future = asyncHelper.asyncReturn(i);
            futures.add(future);
        }

        List<String> response = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                response.add(future.get());
            } catch (InterruptedException e) {
//                e.printStackTrace();
            } catch (ExecutionException e) {
//                e.printStackTrace();
                for (Future<String> f : futures) {
                    if (!f.isDone()) {
                        System.out.println("cancel " + f);
                        f.cancel(true);
                    }
                }
                throw new RuntimeException("xxx");
            }
        }

        Map<String, Object> map = new HashMap<>();
        String res = "time cost: " + (System.currentTimeMillis() - start);
        map.put("data", response);
        map.put("cost", res);
        return map;
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
