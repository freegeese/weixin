package com.github.freegeese.weixin.mp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@RequestMapping("concurrent")
public class ConcurrentController {
    private static Long count;
    private static Long count2;
    private final ReentrantLock lock = new ReentrantLock();

    @GetMapping("/{boke}/{user}")
    @ResponseBody
    public Object doGet(@PathVariable("boke") String boke, @PathVariable("user") String user) {
//        System.out.println("test1+++++++++++++++++++++++++++++++++++++++");
        if (null != count) {
            return count;
        }
        lock.lock();
        if (null != count) {
            return count;
        }
        count = Double.valueOf(Math.random() * 10000000).longValue();
        lock.unlock();
        System.out.println("test1-----------------------------------> " + count);
        return count;
    }

    @GetMapping("/test2")
    @ResponseBody
    public Object doGet2() {
//        System.out.println("test2+++++++++++++++++++++++++++++++++++++++");
        if (null != count2) {
            return count2;
        }
        lock.lock();
        if (null != count2) {
            return count2;
        }
        count2 = Double.valueOf(Math.random() * 10000000).longValue();
        lock.unlock();
        System.out.println("test2-----------------------------------> " + count2);
        return count2;
    }



}
