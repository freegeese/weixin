package com.github.freegeese.weixin.core.test;

import com.github.freegeese.weixin.core.test.pay.PayCodeGenerator;
import org.junit.Test;

public class PayCodeGeneratorTests {

    @Test
    public void test1() throws Exception {
        PayCodeGenerator.generate();
    }

    @Test
    public void test2() throws Exception {
        PayCodeGenerator.getApiUrls();
    }

    @Test
    public void test3() throws Exception {
        PayCodeGenerator.getCommonParameters();
    }

}
