package com.wl.rest_backend_model.utils;

import com.wl.rest_backend_model.utils.rest.Filter;
import com.wl.rest_backend_model.utils.rest.FilterSpecification;
import com.wl.rest_backend_model.utils.rest.RestPage;
import com.wl.rest_backend_model.utils.rest.RestPageRequestBody;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RepositorySearchUtils {
    public static <T> RestPage page(JpaSpecificationExecutor<T> specExecutor, RestPageRequestBody body) {
        if (body != null) {
            Filter filter = RestControllerUtils.toFilter(body.getFilter());
            Sort sort = RestControllerUtils.toSort(body.getSort());
            Pageable pageRequest = RestControllerUtils.toPageable(body.getPageRequest(), sort);
            FilterSpecification spec = new FilterSpecification(filter, null);
            return RestControllerUtils.toRestPage(specExecutor.findAll(spec, pageRequest));
        } else {
            FilterSpecification spec = new FilterSpecification(Filter.nofilter(), null);
            return RestControllerUtils.toRestPage(specExecutor.findAll(spec, Pageable.unpaged()));
        }
    }
}
