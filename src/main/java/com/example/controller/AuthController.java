package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.entity.UserPrincipal;
import com.example.service.UserService;
import com.example.util.JwtUtil;

@RestController
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			userService.save(user);
			return ResponseEntity.ok("Register Successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		try {
			UserPrincipal userPrincipal = userService.signInWithUsernameAndPassword(
					user.getUsername(), user.getPassword());

			String token = jwtUtil.generateAccessToken(userPrincipal);
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("username", userPrincipal.getUsername());
			response.put("token", token);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

	@GetMapping("/user-read")
	@PreAuthorize("hasAnyAuthority('READ_PERMISSION')")
	public ResponseEntity<String> userRead() {
		return ResponseEntity.ok("User Read Accepted!");
	}

}
