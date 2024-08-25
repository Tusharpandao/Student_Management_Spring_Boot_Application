package com.techeazy.studentmanagement.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techeazy.studentmanagement.entity.Student;
import com.techeazy.studentmanagement.entity.Subject;
import com.techeazy.studentmanagement.exception.DuplicateSubjectException;
import com.techeazy.studentmanagement.exception.SubjectNotFoundException;
import com.techeazy.studentmanagement.repository.SubjectRepository;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository subjectRepository;

	public List<Subject> getAllSubjects() {
		return subjectRepository.findAll();
	}

	// Method to add a subject, ensuring uniqueness
	public Subject addSubject(Subject subject) throws DuplicateSubjectException {
		Optional<Subject> existingSubject = subjectRepository.findByName(subject.getName());
		if (existingSubject.isPresent()) {
			throw new DuplicateSubjectException("Subject with name " + subject.getName() + " already exists.");
		}
		return subjectRepository.save(subject);
	}

	public List<Subject> addSubjects(List<Subject> subjects) {

		// Fetch all existing subjects from the database
		List<Subject> existingSubjects = subjectRepository.findAll();

		// Create a map to quickly look up existing subjects by name
		Map<String, Subject> existingSubjectMap = existingSubjects.stream()
				.collect(Collectors.toMap(Subject::getName, Function.identity()));

		// Filter out subjects that already exist in the database
		List<Subject> newSubjects = subjects.stream()
				.filter(subject -> !existingSubjectMap.containsKey(subject.getName())).collect(Collectors.toList());

		// Save and return only the new subjects
		return subjectRepository.saveAll(newSubjects);
	}

	public Subject getSubjectById(Long id) {

		return subjectRepository.findById(id).orElse(null);
	}

	public Subject deleteSubject(Long id) {
		Subject subject = subjectRepository.findById(id).orElse(null);

		if (subject != null) {
			// Remove the subject from each student's list of subjects
			for (Student student : subject.getStudents()) {
				student.getSubjects().remove(subject);
			}
			subjectRepository.delete(subject);
			return subject;
		}
		return null;
	}

//	 // Method to delete a subject with cascading removal from associated students
//    @Transactional
//    public void deleteSubject(Long subjectId) {
//        Subject subject = subjectRepository.findById(subjectId)
//            .orElseThrow(() -> new SubjectNotFoundException("Subject not found with id: " + subjectId));
//
//        // Remove the subject from all associated students
//        List<Student> students = studentRepository.findAllBySubjectsContaining(subject);
//        for (Student student : students) {
//            student.getSubjects().remove(subject);
//            studentRepository.save(student);
//        }
//
//        // Delete the subject
//        subjectRepository.delete(subject);
//    }

}
