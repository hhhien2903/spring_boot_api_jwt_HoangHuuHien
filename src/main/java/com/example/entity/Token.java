package com.example.entity;

import java.util.Date;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token extends BaseEntity {

	@Column(length = 1000)
	private String token;

	private Date tokenExpDate;

}
