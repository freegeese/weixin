package com.github.freegeese.weixin.mp.domain;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class ResponseEntity<ID extends Serializable> extends BaseEntity<ID> {
    private ID request_id;

    public ID getRequest_id() {
        return request_id;
    }

    public void setRequest_id(ID request_id) {
        this.request_id = request_id;
    }
}
