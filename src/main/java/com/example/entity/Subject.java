package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "t_subject")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Subject extends BaseEntity {
	private String name;
	private String credit;
}
