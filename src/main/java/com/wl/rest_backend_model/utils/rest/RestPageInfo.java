package com.wl.rest_backend_model.utils.rest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestPageInfo {
    private int currentPage;
    private int totalPages;
    private int size;
    private long totalElements;
}
