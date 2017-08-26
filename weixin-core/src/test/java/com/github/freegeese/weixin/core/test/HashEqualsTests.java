package com.github.freegeese.weixin.core.test;

import com.github.freegeese.weixin.core.dto.GetTokenRequest;
import org.junit.Test;

public class HashEqualsTests {

    @Test
    public void test() throws Exception {
        GetTokenRequest request = new GetTokenRequest();
        request.setAppid("x");
        GetTokenRequest request1 = new GetTokenRequest();
        request1.setAppid("x");
        System.out.println(request.equals(request1));
        System.out.println(request == request1);

    }


}
