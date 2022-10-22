package com.example.service;

import java.util.List;

import com.example.entity.Subject;

public interface SubjectService {
	Subject save(Subject subject);

	List<Subject> findAll();
}
