package com.datn.motchill.controller;

import com.datn.motchill.config.security.JwtTokenProvider;
import com.datn.motchill.dto.AuthResponse;
import com.datn.motchill.dto.user.LoginRequest;
import com.datn.motchill.dto.user.RegisterRequest;
import com.datn.motchill.dto.user.UserDTO;
import com.datn.motchill.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        // Chuyển đổi RegisterRequest thành UserRequest
        UserDTO userRequest = new UserDTO();
        userRequest.setUsername(request.getUsername());
        userRequest.setEmail(request.getEmail());
        userRequest.setPassword(request.getPassword());
        userRequest.setFullName(request.getFullName());
        userRequest.setAvatar(request.getAvatar());

        // Tạo người dùng mới
        UserDTO createdUser = userService.save(userRequest);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Xác thực người dùng
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Lưu thông tin xác thực vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo JWT token
        String jwt = tokenProvider.generateToken(authentication);

        // Lấy thông tin người dùng
        UserDTO userDto = userService.findByUsername(loginRequest.getUsername());

        // Trả về token và thông tin người dùng
        return ResponseEntity.ok(new AuthResponse(jwt, userDto));
    }
}
