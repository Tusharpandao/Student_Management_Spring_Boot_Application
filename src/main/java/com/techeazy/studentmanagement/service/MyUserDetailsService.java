package com.techeazy.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techeazy.studentmanagement.entity.UserPrincipal;
import com.techeazy.studentmanagement.entity.Users;
import com.techeazy.studentmanagement.repository.UserRepo;
@Service
public  class MyUserDetailsService implements UserDetailsService {
	   
	  @Autowired
	    private UserRepo userRepository;

	  
	  @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Users user = userRepository.findByUserName(username);
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found");
	        }
	        return new UserPrincipal(user);
	    }
}
