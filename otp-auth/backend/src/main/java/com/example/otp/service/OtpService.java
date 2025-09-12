package com.example.otp.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Service
public class OtpService {

    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    @Value("${twilio.accountSid}")
    private String twilioSid;

    @Value("${twilio.authToken}")
    private String twilioAuthToken;

    @Value("${twilio.phoneNumber}")
    private String twilioFromNumber;

    @Value("${jwt.secret:mysecretkey}")
    private String jwtSecret;

    private static final long OTP_TTL_MS = 5 * 60 * 1000; // 5 minutes
    private static final long JWT_TTL_MS = 15 * 60 * 1000; // 15 minutes

    @PostConstruct
    public void init() {
        if (twilioSid == null || twilioAuthToken == null || twilioFromNumber == null) {
            throw new RuntimeException("Twilio configuration missing!");
        }
        Twilio.init(twilioSid, twilioAuthToken);
    }

    public void generateAndSendOtp(String phone) {
        String otp = String.valueOf(100000 + random.nextInt(900000));
        otpStore.put(phone, otp);

        Message.creator(
                new PhoneNumber(phone),
                new PhoneNumber(twilioFromNumber),
                "Your verification code is: " + otp
        ).create();
    }

    public String verifyOtpAndCreateToken(String phone, String otp) {
        String stored = otpStore.get(phone);
        if (stored != null && stored.equals(otp)) {
            otpStore.remove(phone);
            return Jwts.builder()
                    .setSubject(phone)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TTL_MS))
                    .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                    .compact();
        }
        return null;
    }
}
