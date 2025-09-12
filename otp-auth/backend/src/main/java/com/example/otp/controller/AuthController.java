package com.example.otp.controller;

import com.example.otp.dto.SendOtpRequest;
import com.example.otp.dto.VerifyOtpRequest;
import com.example.otp.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final OtpService otpService;

    public AuthController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Validated @RequestBody SendOtpRequest req){
        otpService.generateAndSendOtp(req.getPhone());
        return ResponseEntity.ok(Map.of("status","success","message","OTP sent"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Validated @RequestBody VerifyOtpRequest req){
        String token = otpService.verifyOtpAndCreateToken(req.getPhone(), req.getOtp());
        if (token != null){
            return ResponseEntity.ok(Map.of("status","success","message","OTP verified","token", token));
        } else {
            return ResponseEntity.badRequest().body(Map.of("status","error","message","Invalid or expired OTP"));
        }
    }
}
