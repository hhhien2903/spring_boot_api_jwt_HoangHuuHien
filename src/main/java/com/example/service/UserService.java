package com.example.service;

import com.example.entity.User;
import com.example.entity.UserPrincipal;

public interface UserService {
	User save(User user) throws Exception;

	UserPrincipal signInWithUsernameAndPassword(String username, String password)
			throws Exception;

}
