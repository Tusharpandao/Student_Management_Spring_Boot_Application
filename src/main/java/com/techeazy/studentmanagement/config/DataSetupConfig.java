package com.techeazy.studentmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.techeazy.studentmanagement.entity.Role;
import com.techeazy.studentmanagement.entity.Users;
import com.techeazy.studentmanagement.repository.RoleRepo;
import com.techeazy.studentmanagement.repository.UserRepo;

import jakarta.transaction.Transactional;

import java.util.Optional;
@Configuration
public class DataSetupConfig {

    @Bean
    CommandLineRunner setupData(RoleRepo roleRepo, UserRepo userRepo) {
        return args -> {
            createRoleIfNotExist(roleRepo, "ADMIN");
            createRoleIfNotExist(roleRepo, "STUDENT");

            createAdminUserIfNotExist(userRepo, roleRepo);
        };
    }

    @Transactional
    private void createRoleIfNotExist(RoleRepo roleRepo, String roleName) {
        Optional<Role> existingRole = roleRepo.findByName(roleName);
        if (existingRole.isEmpty()) {
            roleRepo.save(new Role(roleName));
        }
    }

    @Transactional
    private void createAdminUserIfNotExist(UserRepo userRepo, RoleRepo roleRepo) {
        Optional<Role> adminRoleOptional = roleRepo.findByName("ADMIN");

        if (adminRoleOptional.isPresent()) {
            Role adminRole = adminRoleOptional.get();

            // Check if an Admin user already exists
            boolean adminUserExists = userRepo.findAll().stream()
                    .anyMatch(user -> user.getRole() != null && user.getRole().equals(adminRole));

            if (!adminUserExists) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
                Users admin = new Users();
                admin.setUserName("admin");
                admin.setPassword(encoder.encode("admin"));
                admin.setRole(adminRole);  // Assign the role directly

                userRepo.save(admin);
            }
        } else {
            System.out.println("ADMIN role not found. Please ensure roles are created.");
        }
    }

    }


