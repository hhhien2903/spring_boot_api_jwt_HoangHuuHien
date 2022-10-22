package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Subject;
import com.example.service.SubjectService;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService subjectService;

	@GetMapping("/subject")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(subjectService.findAll());
	}

	@PostMapping("/subject")
	public ResponseEntity<?> create(@RequestBody Subject subject) {
		return ResponseEntity.status(HttpStatus.OK).body(subjectService.save(subject));
	}
}
