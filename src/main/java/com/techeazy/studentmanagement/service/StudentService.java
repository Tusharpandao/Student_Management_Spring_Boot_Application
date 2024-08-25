package com.techeazy.studentmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techeazy.studentmanagement.entity.Student;
import com.techeazy.studentmanagement.entity.Subject;
import com.techeazy.studentmanagement.exception.ResourceNotFoundException;
import com.techeazy.studentmanagement.exception.StudentDataExist;
import com.techeazy.studentmanagement.repository.StudentRepository;
import com.techeazy.studentmanagement.repository.SubjectRepository;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SubjectRepository subjectRepository;

    public Student saveStudent(Student student) throws StudentDataExist {
        // Fetch all existing subjects from the database
        List<Subject> existingSubjects = subjectRepository.findAll();

        // Create a map to quickly look up existing subjects by name
        Map<String, Subject> existingSubjectMap = existingSubjects.stream()
                .collect(Collectors.toMap(Subject::getName, Function.identity()));

        // Create a list to store the updated subjects
        List<Subject> updatedSubjects = new ArrayList<>();
        for (Subject subject : student.getSubjects()) {
            if (subject.getName() != null) {
                Subject existingSubject = existingSubjectMap.get(subject.getName());
                if (existingSubject != null) {
                    // Attach existing subject
                    updatedSubjects.add(existingSubject);
                }
            }
        }
        // Set the subject to Student after checking if the existing subject is present in the database
        student.setSubjects(updatedSubjects);

        // Check if the student already exists
        List<Student> existingStudents = studentRepository.findAll();
        for (Student existingStudent : existingStudents) {
            if (existingStudent.getEmail().equals(student.getEmail()) || existingStudent.getMobile() == student.getMobile()) {
                throw new StudentDataExist(true, "Student data already present");
            }
        }

        // Save the student if it does not already exist
        return studentRepository.save(student);
    }


	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Student getStudentById(Long id) {
		return studentRepository.findById(id).orElse(null);
	}


	public Student deleteStudent(Long id) {
	    Optional<Student> optionalStudent = studentRepository.findById(id);

	    if (optionalStudent.isPresent()) {
	        Student student = optionalStudent.get();
	        // Fetch subjects to avoid lazy loading issues
	        student.getSubjects().size(); // This forces the lazy-loaded subjects to be fetched
	        studentRepository.delete(student);
	        return student;
	    }
	    return null;
	}



    public Student updateStudent(Long id, Student student) throws ResourceNotFoundException {
        // Find the existing student by ID
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()) {
            throw new ResourceNotFoundException(true, "Student not found with id " + id);
        }

        // Get the existing student
        Student existingStudent = optionalStudent.get();

        // Update the fields
        existingStudent.setName(student.getName());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setMobile(student.getMobile());
        existingStudent.setPassword(student.getPassword());

        // Fetch all existing subjects from the database
        List<Subject> existingSubjects = subjectRepository.findAll();

        // Create a map to quickly look up existing subjects by name
        Map<String, Subject> existingSubjectMap = existingSubjects.stream()
                .collect(Collectors.toMap(Subject::getName, Function.identity()));

        // Create a list to store the updated subjects
        List<Subject> updatedSubjects = new ArrayList<>();
        if (student.getSubjects() != null) {
            for (Subject subject : student.getSubjects()) {
                if (subject.getName() != null) {
                    Subject existingSubject = existingSubjectMap.get(subject.getName());
                    if (existingSubject != null) {
                        // Attach existing subject
                        updatedSubjects.add(existingSubject);
                    }
                }
            }
            existingStudent.setSubjects(updatedSubjects);
        }

        // Save the updated student
        return studentRepository.save(existingStudent);
    }


}
