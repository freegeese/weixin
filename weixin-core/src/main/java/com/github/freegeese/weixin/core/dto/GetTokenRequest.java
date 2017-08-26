package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class GetTokenRequest {
    private String grant_type = "client_credential";
    private String appid;
    private String secret;

    public GetTokenRequest() {
    }

    public GetTokenRequest(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetTokenRequest that = (GetTokenRequest) o;
        if (grant_type != null ? !grant_type.equals(that.grant_type) : that.grant_type != null) return false;
        if (appid != null ? !appid.equals(that.appid) : that.appid != null) return false;
        return secret != null ? secret.equals(that.secret) : that.secret == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (grant_type != null ? grant_type.hashCode() : 0);
        result = 31 * result + (appid != null ? appid.hashCode() : 0);
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        return result;
    }
}
