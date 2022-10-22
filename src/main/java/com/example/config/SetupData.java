package com.example.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.entity.Permission;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.PermissionRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Component
public class SetupData implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if (alreadySetup)
			return;
		Permission readPermission = createPermissionIfNotFound("READ_PERMISSION");
		Permission writePermission = createPermissionIfNotFound("WRITE_PERMISSION");

		List<Permission> adminPermissions = Arrays.asList(readPermission,
				writePermission);
		createRoleIfNotFound("ROLE_ADMIN", adminPermissions);
		createRoleIfNotFound("ROLE_USER", Arrays.asList(readPermission));

		Role adminRole = roleRepository.findByName("ROLE_ADMIN");
		User admin = new User();
		admin.setPassword(bCryptPasswordEncoder.encode("test"));
		admin.setUsername("test");
		admin.setRoles(Arrays.asList(adminRole));
		userRepository.save(admin);

		alreadySetup = true;
	}

	@Transactional
	private Permission createPermissionIfNotFound(String name) {

		Permission permission = permissionRepository.findByName(name);
		if (permission == null) {
			permission = new Permission(name);
			permissionRepository.save(permission);
		}
		return permission;
	}

	@Transactional
	private Role createRoleIfNotFound(String name, Collection<Permission> permissions) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPermissions(permissions);
			roleRepository.save(role);
		}
		return role;
	}

}
