package com.techeazy.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techeazy.studentmanagement.entity.Student;


public interface StudentRepository extends JpaRepository<Student,Long>{

}
