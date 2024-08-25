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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techeazy.studentmanagement.entity.Student;
import com.techeazy.studentmanagement.exception.ResourceNotFoundException;
import com.techeazy.studentmanagement.exception.StudentDataExist;
import com.techeazy.studentmanagement.response.ResponseStructure;
import com.techeazy.studentmanagement.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentService studentService;

	@GetMapping("/welcome")
	public String welcome() {
		String text = "Welcome to Student Management";

		return text;
	}

//	 @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	@PostMapping("/register")
	public ResponseEntity<?> createStudent(@RequestBody Student student) {
		try {
			Student savedStudent = studentService.saveStudent(student);
			if (savedStudent != null) {
				return new ResponseEntity<ResponseStructure<Student>>(
						new ResponseStructure<Student>("Student Added successfully", savedStudent),
						HttpStatus.ACCEPTED);
			}
			return new ResponseEntity<ResponseStructure<Student>>(
					new ResponseStructure<Student>("Student not Added ", savedStudent), HttpStatus.NOT_ACCEPTABLE);
		} catch (StudentDataExist e) {
			
			return new ResponseEntity<ResponseStructure<StudentDataExist>>
				(new ResponseStructure<StudentDataExist>(e.getMsg(),e), HttpStatus.NOT_ACCEPTABLE);
//			
		}catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
	}
	 @PreAuthorize("hasRole('ADMIN')")
	@GetMapping
    public ResponseEntity<?> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        
       if (!students.isEmpty() && students.size() >0) {
		return new ResponseEntity<ResponseStructure<Student>>
		(new ResponseStructure<Student>("All Student Data found", students),HttpStatus.FOUND);
	}
       return new ResponseEntity<ResponseStructure<Student>>
		(new ResponseStructure<Student>("Student Data Not Found Or Student Data Emty", students),HttpStatus.NOT_FOUND);
    }
	 @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable Long id) {
		 Student student = studentService.getStudentById(id);
		 
		 if (student!= null) {
             return new ResponseEntity<ResponseStructure<Student>>
             (new ResponseStructure<Student>("Student Data Found successfully", student),HttpStatus.FOUND);
             }
		 return new ResponseEntity<ResponseStructure<Student>>
         (new ResponseStructure<Student>("Student Not Found ....! ", student),HttpStatus.FOUND);
		 
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
		Student deleteStudent = studentService.deleteStudent(id);
		if(deleteStudent != null) {
			return new ResponseEntity<ResponseStructure<Student>>
				(new ResponseStructure<Student>("Student Data Deleted successfully", deleteStudent),HttpStatus.OK);
		}
		return new ResponseEntity<ResponseStructure<Student>>
		(new ResponseStructure<Student>("Student Data Not Found", deleteStudent),HttpStatus.NOT_FOUND);

	}

	 @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	 @PutMapping("/{id}")
	    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
	        try {
	            Student updatedStudent = studentService.updateStudent(id, student);
	            return ResponseEntity.ok(updatedStudent);
	        } catch (ResourceNotFoundException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	    }

   
}
