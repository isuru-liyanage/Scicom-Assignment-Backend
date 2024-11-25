package com.example.demo.controllers;

import com.example.demo.dto.AuthResponceDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.SignupDTO;
import com.example.demo.dto.SingleLineResponceDTO;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {


    private final AuthenticationService authenticationService;

    @PostMapping ("/register")
    public ResponseEntity<SingleLineResponceDTO> Signup(@RequestBody @Valid SignupDTO request ) {

        return authenticationService.signup(request);
    }


    @PostMapping ("/login")
    public ResponseEntity<AuthResponceDTO> Login(@RequestBody @Valid LoginDTO request) {
        return authenticationService.login(request);
    }

//    @PostMapping("/change_password")
//    public ResponseEntity<SingleLineResponceDTO> changePassword(@RequestBody PasswordChangeDTO request, @AuthenticationPrincipal UserDetails userDetails) {
//        return authenticationService.changePassword(request,userDetails);
//    }

    @GetMapping("/userdetails")
    public ResponseEntity<UserDetails> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userDetails);
    }
    @GetMapping("/myrole")
    public ResponseEntity<Map<String,String>> getUserRole(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No user authenticated"));
        }
        return ResponseEntity.ok(Map.of("role", userDetails.getAuthorities().iterator().next().getAuthority()));
    }

}
