package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
	public Permission findByName(String name);
}
