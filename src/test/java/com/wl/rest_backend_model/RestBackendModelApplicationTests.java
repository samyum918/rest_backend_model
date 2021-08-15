package com.wl.rest_backend_model;

import com.wl.rest_backend_model.model.User;
import com.wl.rest_backend_model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RestBackendModelApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
		User user = userRepository.findById(1).orElse(null);
		assertNotNull(user);
		System.out.println(user);
	}
}
