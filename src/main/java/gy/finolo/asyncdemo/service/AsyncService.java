package gy.finolo.asyncdemo.service;

import java.util.Map;
import java.util.concurrent.Future;

public interface AsyncService {

    void syncNoReturn();

    void asyncNoReturn();

    Future<String> asyncReturn(int i);

    Map<String, Object> asyncReturn2();

}
