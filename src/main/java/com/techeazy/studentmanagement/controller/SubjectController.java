package com.techeazy.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techeazy.studentmanagement.entity.Student;
import com.techeazy.studentmanagement.entity.Subject;
import com.techeazy.studentmanagement.response.ResponseStructure;
import com.techeazy.studentmanagement.service.SubjectService;

@RestController
@RequestMapping("/subjects")
public class SubjectController {
	@Autowired
	private SubjectService subjectService;

	 @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<?> getAllSubjects() {
		List<Subject> subjects = subjectService.getAllSubjects();

		if (!subjects.isEmpty() && subjects.size() > 0) {
			return new ResponseEntity<ResponseStructure<Subject>>(
					new ResponseStructure<Subject>("All Subject Found Successfully", subjects), HttpStatus.FOUND);

		}
		return new ResponseEntity<ResponseStructure<Student>>(
				new ResponseStructure<Student>(" Subject Not Found ", subjects), HttpStatus.NOT_FOUND);

	}
	 
	 @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	@GetMapping("/{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Long id) {
		Subject subject = subjectService.getSubjectById(id);
		
        if (subject!= null) {
             return new ResponseEntity<ResponseStructure<Subject>>
             (new ResponseStructure<Subject>("Subject Found Successfully", subject), HttpStatus.FOUND);
             }
        return new ResponseEntity<ResponseStructure<Subject>>(
                new ResponseStructure<Subject>("Subject Not Found ....! ", subject), HttpStatus.FOUND);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<?> addSubjects(@RequestBody List<Subject> subject) {

		List<Subject> subjects = subjectService.addSubjects(subject);

		if (!subjects.isEmpty() && subjects.size() > 0) {
			return new ResponseEntity<ResponseStructure<Subject>>(
					new ResponseStructure<Subject>("Subject Added Successfully", subjects), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<ResponseStructure<Subject>>(
				new ResponseStructure<Subject>("Failed to Add Subject", subjects), HttpStatus.NOT_ACCEPTABLE);
				
			
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSubject(@PathVariable Long id) {
		Subject subject = subjectService.deleteSubject(id);
		
        if(subject!=null) {
        		return new ResponseEntity<ResponseStructure<Subject>>(
                    new ResponseStructure<Subject>("Subject Deleted Successfully", subject), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<ResponseStructure<Subject>>(
                new ResponseStructure<Subject>("Failed to Delete Subject", subject), HttpStatus.NOT_ACCEPTABLE);
		}

}
