package com.github.freegeese.weixin.core.dto;

import lombok.Data;

@Data
public class GetTicketRequest {
    private String type = "jsapi";
    private String access_token;

    public GetTicketRequest() {
    }

    public GetTicketRequest(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetTicketRequest that = (GetTicketRequest) o;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return access_token != null ? access_token.equals(that.access_token) : that.access_token == null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (access_token != null ? access_token.hashCode() : 0);
        return result;
    }
}
