package com.ning.spring.orm.framework;

import java.io.Serializable;
import java.util.List;

/**
 * @Author JAY
 * @Date 2019/6/29 10:21
 * @Description TODO
 **/
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pageNum;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private int pageSize = DEFAULT_PAGE_SIZE; // 每页的记录数

    private long totalCount;

    private List<T> data;
    //一共多少页
    private long totalPage;

    public Page(int pageNum, int pageSize, long totalCount, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
        this.totalPage = getTotalPage(pageSize,totalCount);
    }

    private long getTotalPage(int pageSize, long totalCount) {
        long page = totalCount % pageSize;
        if (page > 0){
            return totalCount / pageSize + 1;
        }else {
            return totalCount / pageSize;
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", data=" + data +
                ", totalPage=" + totalPage +
                '}';
    }
}
