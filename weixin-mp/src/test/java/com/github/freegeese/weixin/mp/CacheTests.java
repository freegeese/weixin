package com.github.freegeese.weixin.mp;

import com.google.common.cache.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class CacheTests {

    @Test
    public void test() throws Exception {
        LoadingCache<String, Object> graphs = CacheBuilder.newBuilder()
                .maximumSize(5)
                // 在访问多长时间后过期
//                .expireAfterAccess(2, TimeUnit.SECONDS)
                // 在写入多少时间后过期
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build(
                        new CacheLoader<String, Object>() {
                            @Override
                            public Object load(String key) throws Exception {
                                return RandomStringUtils.randomAlphanumeric(6);
                            }
                        }
                );

        for (int i = 0; i < 10; i++) {
            System.out.println(graphs.get("3"));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test2() throws Exception {
        Cache<Object, Object> graphs = CacheBuilder.newBuilder()
//                .maximumSize(10)
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();
        for (int i = 0; i < 10; i++) {
            Object value = graphs.get("3", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return RandomStringUtils.randomAlphanumeric(6);
                }
            });
            System.out.println(value);
            Thread.sleep(1000);
        }
    }

    @Test
    public void test3() throws Exception {
        LoadingCache<String, Object> graphs = CacheBuilder.newBuilder()
                .maximumWeight(5)
                .weigher(new Weigher<String, Object>() {
                    @Override
                    public int weigh(String key, Object value) {
                        return 2;
                    }
                })
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Object> notification) {
                        System.out.println(notification.getKey() + "-------->被移除");
                    }
                })
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return RandomStringUtils.randomAlphanumeric(6);
                    }
                });
        for (int j = 0; j < 8; j++) {
            System.out.println(graphs.get(String.valueOf(j)));
            Thread.sleep(1000);
        }
    }

    @Test
    public void test4() throws Exception {
        Cache<String, Object> graphs = CacheBuilder.newBuilder()
                .maximumSize(100)
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Object> notification) {
                        System.out.println(notification.getKey() + "--->被移除");
                    }
                })
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return RandomStringUtils.randomAlphanumeric(6);
                    }
                });

        for (int i = 0; i < 10; i++) {
            Object value = graphs.get(String.valueOf(i), new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return RandomStringUtils.randomAlphanumeric(6);
                }
            });
        }
        graphs.invalidate("1");
        graphs.invalidateAll();
    }

    @Test
    public void test5() throws Exception {
        LoadingCache<String, Object> graphs = CacheBuilder.newBuilder()
                .maximumSize(10)
                .refreshAfterWrite(3, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, Object> notification) {
                        System.out.println(notification.getKey() + "---->被移除");
                    }
                })
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return RandomStringUtils.randomAlphanumeric(6);
                    }
                });

        System.out.println(graphs.get("3"));
        Thread.sleep(5000);
        System.out.println("xxxxxxxxxxxxxx");
        Thread.sleep(10000);
        System.out.println(graphs.get("3"));
    }

}
