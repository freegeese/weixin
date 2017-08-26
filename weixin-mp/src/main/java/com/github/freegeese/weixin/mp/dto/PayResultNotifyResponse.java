package com.github.freegeese.weixin.mp.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;

@XStreamAlias("xml")
@Getter
public class PayResultNotifyResponse {
    private String return_code;
    private String return_msg;

    public PayResultNotifyResponse(boolean success) {
        this.return_code = success ? "SUCCESS" : "FAIL";
    }


    public PayResultNotifyResponse message(String message) {
        this.return_msg = message;
        return this;
    }

    public static PayResultNotifyResponse success() {
        return success("OK");
    }

    public static PayResultNotifyResponse success(String message) {
        return new PayResultNotifyResponse(true).message(message);
    }

    public static PayResultNotifyResponse failure() {
        return failure("Error");
    }

    public static PayResultNotifyResponse failure(String message) {
        return new PayResultNotifyResponse(false).message(message);
    }
}
