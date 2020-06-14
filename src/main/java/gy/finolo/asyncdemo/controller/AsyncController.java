package gy.finolo.asyncdemo.controller;

import gy.finolo.asyncdemo.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
