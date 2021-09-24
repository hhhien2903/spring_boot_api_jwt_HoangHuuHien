package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_permission")
@Getter
@Setter
public class Permission extends BaseEntity {

	private String permissionName;

	private String permissionKey;

}