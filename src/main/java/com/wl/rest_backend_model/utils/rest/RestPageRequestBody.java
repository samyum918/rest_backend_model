package com.wl.rest_backend_model.utils.rest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestPageRequestBody {
    private RestFilter filter;
    private RestPageRequest pageRequest;
    private RestSort sort;
}
