package com.github.freegeese.weixin.mp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 公共的请求参数
 */
@ApiModel
public class Request {
    // 请求凭证
    @ApiModelProperty(value = "请求凭证", example = "123456abcdefg", required = false)
    private String token;
    // 请求的接口版本（暂时不会用到）
    @ApiModelProperty(value = "接口版本", example = "1.0 | v1")
    private String version;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
