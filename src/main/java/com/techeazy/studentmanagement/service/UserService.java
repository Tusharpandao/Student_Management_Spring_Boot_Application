package com.techeazy.studentmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techeazy.studentmanagement.entity.Role;
import com.techeazy.studentmanagement.entity.Users;
import com.techeazy.studentmanagement.repository.RoleRepo;
import com.techeazy.studentmanagement.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private UserRepo repo;

	@Autowired
	private JWTService jwtService;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	private RoleRepo roleRepo;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

	public Users register(Users user) {

		Role role = roleRepo.findByName(user.getRole().getName())
				.orElseThrow(() -> new RuntimeException("Role not found"));

		user.setRole(role);
		user.setPassword(encoder.encode(user.getPassword()));
		repo.save(user);
		return user;
	}

//	public String verify(Users user) {
//		Authentication authentication = authManager
//				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//
//		if (authentication.isAuthenticated()) {
//			return jwtService.generateToken(user.getUserName());
//
//		}
//
//		return "Invalid Credentials";
//
//	}

	@Transactional
	public void performOperation() {
		// Access the collection within a transaction
		Optional<Role> roleOptional = roleRepo.findByName("ADMIN");
		roleOptional.ifPresent(role -> {
			// Work with the role and its users here
			Set<Users> users = role.getUsers(); // This will initialize the collection
		});
	}

	public String verify(Users user) {
		Authentication authentication = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

		if (authentication.isAuthenticated()) {
			List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());
			return jwtService.generateToken(user.getUserName(), roles);
		}

		return "Invalid Credentials";
	}

}
