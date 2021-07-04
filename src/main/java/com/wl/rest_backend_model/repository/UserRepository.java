package com.wl.rest_backend_model.repository;

import com.wl.rest_backend_model.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends EntityRepository<User, Integer> {
}
