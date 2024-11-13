package com.brix.Seller_Sync.common.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean isLast;

    public PagedResponse() {

    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean isLast) {
        setContent(content);
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
    }

    public List<T> getContent() {
        return content == null ? null : new ArrayList<>(content);
    }

    public final void setContent(List<T> content) {
        if (content == null) {
            this.content = null;
        } else {
            this.content = Collections.unmodifiableList(content);
        }
    }

    public boolean isLast() {
        return isLast;
    }
}
