package com.github.freegeese.weixin.mp.dto;

/**
 * 分页请求对象
 */
public class PageRequest extends Request {
    // 第几页
    private Integer pageNo;
    // 每页多少条记录
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
