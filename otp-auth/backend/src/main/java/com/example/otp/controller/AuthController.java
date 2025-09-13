package com.example.otp.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.otp.dto.SendOtpRequest;
import com.example.otp.dto.VerifyOtpRequest;
import com.example.otp.service.OtpService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final OtpService otpService;

    public AuthController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Validated @RequestBody SendOtpRequest req) {
        try {
            otpService.generateAndSendOtp(req.getPhone());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "OTP sent successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Failed to send OTP: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Validated @RequestBody VerifyOtpRequest req) {
        try {
            String token = otpService.verifyOtpAndCreateToken(req.getPhone(), req.getOtp());
            if (token != null) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "OTP verified successfully",
                        "token", token
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Invalid or expired OTP"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Verification failed: " + e.getMessage()
            ));
        }
    }
}

