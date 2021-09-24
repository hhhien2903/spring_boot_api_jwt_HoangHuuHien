package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.authen.UserPrincipal;
import com.example.entity.Token;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.JwtUtil;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public User register(@RequestBody User user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

		return userService.createUser(user);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {

		UserPrincipal userPrincipal = userService.findByUsername(user.getUsername());

		if (null == user || !new BCryptPasswordEncoder().matches(user.getPassword(),
				userPrincipal.getPassword())) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Account or password is not valid!");
		}

		Token token = new Token();
		token.setToken(jwtUtil.generateToken(userPrincipal));

		token.setTokenExpDate(jwtUtil.generateExpirationDate());
		token.setCreatedBy(userPrincipal.getUserId());

		return ResponseEntity.ok(token.getToken());
	}

}
