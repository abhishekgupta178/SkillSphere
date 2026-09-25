package com.skillsphere.service;

import com.skillsphere.entity.Student;
import com.skillsphere.entity.UserAccount;
import com.skillsphere.repository.StudentRepository;
import com.skillsphere.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserAccountRepository userRepo;
    private final StudentRepository studentRepo;

    public AuthService(UserAccountRepository userRepo, StudentRepository studentRepo){
        this.userRepo=userRepo;
        this.studentRepo=studentRepo;
    }

    @Transactional
    public UserAccount register(String name, String email, String password, String role){
        if(name == null || name.isBlank()) throw new IllegalArgumentException("Full name is required");
        if(email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
        if(password == null || password.isBlank()) throw new IllegalArgumentException("Password is required");
        if(userRepo.findByEmail(email.trim()).isPresent())
            throw new IllegalArgumentException("Email already registered");

        UserAccount user = new UserAccount();
        user.setEmail(email.trim());
        user.setPassword(password);
        user.setRole(role == null || role.isBlank() ? "STUDENT" : role);
        UserAccount saved = userRepo.save(user);

        Student student = new Student();
        student.setUserId(saved.getId());
        student.setName(name.trim());
        studentRepo.save(student);
        return saved;
    }

    public UserAccount login(String email, String password){
        return userRepo.findByEmail(email)
            .filter(u -> u.getPassword().equals(password))
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    }

    public UserAccount get(Long id){return userRepo.findById(id).orElseThrow();}
}
