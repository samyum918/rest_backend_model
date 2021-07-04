package com.wl.rest_backend_model.utils.rest;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RestPageRequest {
    private int page;
    private int size;
}
