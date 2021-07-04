package com.wl.rest_backend_model.utils.rest;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestPage<T> {
    private RestPageInfo pageInfo;
    private List<T> contents;
}
