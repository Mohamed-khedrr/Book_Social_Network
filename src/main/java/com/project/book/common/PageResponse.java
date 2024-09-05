package com.project.book.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content ;
    private int pageNumber;
    private int pageSize;
    private long totalPages;
    private long totalElements;
    private boolean first ;
    private boolean last;
}
