package gy.finolo.asyncdemo.controller;

import gy.finolo.asyncdemo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: Simon
 * @date: 2020-06-14 22:51
 */
@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("sync-no-return")
    public String testSyncNoReturn() {
        long start = System.currentTimeMillis();
        asyncService.syncNoReturn();
        long end = System.currentTimeMillis();
        String res = "time cost: " + (end - start);
        System.out.println(res);
        return res;
    }

    @GetMapping("async-no-return")
    public String testAsyncNoReturn() {
        long start = System.currentTimeMillis();
        asyncService.asyncNoReturn();
        long end = System.currentTimeMillis();
        String res = "time cost: " + (end - start);
        System.out.println(res);
        return res;
    }

    @GetMapping("async-return")
    public Map<String, Object> testAsyncReturn() {
        long start = System.currentTimeMillis();

        List<Future<String>> futures = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Future<String> future = asyncService.asyncReturn(i);
            futures.add(future);
        }

        List<String> response = new ArrayList<>();
        for (Future<String> future: futures) {
            try {
                response.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();

        Map<String, Object> map = new HashMap<>();
        String res = "time cost: " + (end - start);
        map.put("data", response);
        map.put("cost", res);
        return map;
    }


    @GetMapping("async-return2")
    public Map<String, Object> testAsyncReturn2() {
        long start = System.currentTimeMillis();

        return asyncService.asyncReturn2();

    }

}
