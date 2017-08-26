package com.github.freegeese.weixin.mp.dto;

/**
 * 公共的响应对象
 */
public class Response {
    // 相应状态：true->成功，false->失败
    private Boolean success;
    // 响应消息
    private String message;
    // 响应数据
    private Object data;

    public Response(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Response message(String message) {
        this.message = message;
        return this;
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }

    public static Response success() {
        return success("OK");
    }

    public static Response success(String message) {
        return new Response(true).message(message);
    }

    public static Response failure() {
        return failure("Failure");
    }

    public static Response failure(String message) {
        return new Response(false).message(message);
    }
}
