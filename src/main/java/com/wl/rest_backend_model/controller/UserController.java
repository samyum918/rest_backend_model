package com.wl.rest_backend_model.controller;

import com.wl.rest_backend_model.model.User;
import com.wl.rest_backend_model.repository.UserRepository;
import com.wl.rest_backend_model.utils.RepositorySearchUtils;
import com.wl.rest_backend_model.utils.rest.RestPage;
import com.wl.rest_backend_model.utils.rest.RestPageRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/search/page")
    public RestPage<User> pageMerchantAdjustment(@RequestBody() @Valid() RestPageRequestBody body) {
        return RepositorySearchUtils.<User>page(body, (spec, pageRequest) -> userRepository.findAll(spec, pageRequest));
    }

    @PostMapping
    public User createMerchantAdjustment(@RequestBody() @Valid() User entity) {
        return userRepository.save(entity);
    }
}
