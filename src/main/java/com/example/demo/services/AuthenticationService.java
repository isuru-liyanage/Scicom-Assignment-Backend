package com.example.demo.services;

import com.example.demo.dto.AuthResponceDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.SignupDTO;
import com.example.demo.dto.SingleLineResponceDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtHelper;
import com.example.demo.util.Role;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;




    public ResponseEntity<AuthResponceDTO> login(LoginDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtHelper.genarateToken(user);


        HttpCookie cookie = ResponseCookie.from("Authorization", token)
                .path("/")
                .sameSite("None")
                .secure(false)
                .httpOnly(true)
                .maxAge(3600*24*2)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponceDTO(token, user.getRole().toString()));



    }


    public ResponseEntity<SingleLineResponceDTO> signup(SignupDTO request) {



//        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);
//        System.out.println("Password is :  " + randomAlphanumeric);

        User user =new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setDisplayName(request.getDisplayName());
        user.setRole(Role.USER);
        try {
            userRepository.save(user);

        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Username or email already exists");
        }





        return ResponseEntity.ok()
                .body(new SingleLineResponceDTO("User registered successfully"));

    }


//    public ResponseEntity<SingleLineResponceDTO> changePassword(PasswordChangeDTO request, UserDetails userDetails) {
//        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
//        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Incorrect old password");
//        }
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//        return ResponseEntity.ok(new SingleLineResponceDTO("Password changed successfully"));
//    }
}
