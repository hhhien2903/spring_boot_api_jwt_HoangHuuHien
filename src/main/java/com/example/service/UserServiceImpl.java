package com.example.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.UserPrincipal;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User save(User user) throws Exception {
		User existUser = userRepository.findByUsername(user.getUsername());
		if (existUser != null) {
			throw new Exception("Account exists!");
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByName("ROLE_USER");
		user.setRoles(Arrays.asList(userRole));
		return userRepository.save(user);
	}

	@Override
	public UserPrincipal signInWithUsernameAndPassword(String username, String password)
			throws Exception {
		User user = userRepository.findByUsername(username);
		UserPrincipal userPrincipal = new UserPrincipal();

		if (user == null
				|| !bCryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new Exception("Account not found or user/password is not valid!");
		}

		userPrincipal.setId(user.getId());
		userPrincipal.setUsername(user.getUsername());
		userPrincipal.setPassword(user.getPassword());
		userPrincipal.setAuthorities(getAuthorities(user.getRoles()));

		return userPrincipal;
	}

	private Set<String> getAuthorities(Collection<Role> roles) {
		Set<String> permissions = new HashSet<>();

		for (Role role : roles) {
			permissions.add(role.getName());
			role.getPermissions().forEach((permission) -> {
				permissions.add(permission.getName());
			});
		}

		return permissions;
	}
}