package com.techeazy.studentmanagement.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.techeazy.studentmanagement.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(String name);

}
