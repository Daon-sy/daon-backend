package com.daon.backend.common.response.slice;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> extends SliceResponse<T> {

    private int totalPage;
    private int totalCount;

    public PageResponse(Page<T> page) {
        super(page);
        this.totalPage = page.getTotalPages();
        this.totalCount = (int) page.getTotalElements();
    }
}
