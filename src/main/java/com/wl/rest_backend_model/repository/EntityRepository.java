package com.wl.rest_backend_model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
