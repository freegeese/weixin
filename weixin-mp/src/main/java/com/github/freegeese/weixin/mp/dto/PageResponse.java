package com.github.freegeese.weixin.mp.dto;

/**
 * 分页响应对象
 */
public class PageResponse extends Response {
    // 第几页
    private Integer pageNo;
    // 每页多少条记录
    private Integer pageSize;
    // 总记录数
    private Long total;

    public PageResponse(Boolean success) {
        super(success);
    }

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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
