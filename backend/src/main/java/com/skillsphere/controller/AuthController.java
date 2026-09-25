package com.skillsphere.controller;

import com.skillsphere.entity.UserAccount;
import com.skillsphere.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service){this.service=service;}

    public record RegistrationRequest(String name, String email, String password, String role) {}

    @PostMapping("/register")
    public UserAccount register(@RequestBody RegistrationRequest body){
        return service.register(body.name(), body.email(), body.password(), body.role());
    }

    @PostMapping("/login")
    public UserAccount login(@RequestBody Map<String,String> body){
        return service.login(body.get("email"), body.get("password"));
    }

    @GetMapping("/user/{id}")
    public UserAccount user(@PathVariable Long id){return service.get(id);}
}
