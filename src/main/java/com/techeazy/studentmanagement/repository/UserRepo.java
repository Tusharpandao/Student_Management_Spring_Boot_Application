package com.techeazy.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techeazy.studentmanagement.entity.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer>{

	 Users findByUserName(String userName);

	

}
 