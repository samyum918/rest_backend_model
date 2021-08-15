package com.wl.rest_backend_model.utils;

import com.wl.rest_backend_model.utils.rest.Filter;
import com.wl.rest_backend_model.utils.rest.FilterSpecification;
import com.wl.rest_backend_model.utils.rest.RestPage;
import com.wl.rest_backend_model.utils.rest.RestPageRequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.BiFunction;

public class RepositorySearchUtils {
    public static <T> RestPage page(RestPageRequestBody body, BiFunction<Specification<T>, Pageable, Page<T>> searchFunction) {
        if (body != null) {
            Filter filter = RestControllerUtils.toFilter(body.getFilter());
            Sort sort = RestControllerUtils.toSort(body.getSort());
            Pageable pageRequest = RestControllerUtils.toPageable(body.getPageRequest(), sort);
            return RestControllerUtils.toRestPage(searchFunction.apply(new FilterSpecification(filter), pageRequest));
        } else {
            return RestControllerUtils.toRestPage(searchFunction.apply(new FilterSpecification(Filter.nofilter()), Pageable.unpaged()));
        }
    }
}
