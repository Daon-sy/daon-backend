package com.daon.backend.common.response.slice;

import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class SliceResponse<T> {

    private boolean first;
    private boolean last;
    private int pageSize;
    private int pageNumber;
    private int contentSize;
    private List<T> content;

    public SliceResponse(Slice<T> slice) {
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.pageSize = slice.getSize();
        this.pageNumber = slice.getNumber();
        this.contentSize = slice.getNumberOfElements();
        this.content = slice.getContent();
    }
}
