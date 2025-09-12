# OTP Authentication

This project contains both backend (Spring Boot + JWT + Twilio) and frontend (React) for OTP login.

## Prerequisites
- Java 17
- Maven 3.6+
- Node.js 18+
- npm
- Git
- Twilio account (with Account SID, Auth Token, and a phone number)

## Setup

### Backend
1. Go to `backend/`
2. Update `src/main/resources/application.properties` with your Twilio credentials:
   ```properties
   twilio.account-sid=ACxxxxxxxxxxxxxxxx
   twilio.auth-token=your_auth_token
   twilio.from-number=+1234567890
   jwt.secret=replace_with_strong_secret
   ```
3. Run:
   ```bash
   mvn spring-boot:run
   ```
4. Backend will start on `http://localhost:8080`

### Frontend
1. Go to `frontend/`
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run:
   ```bash
   npm start
   ```
4. Open `http://localhost:3000` in browser

## Flow
1. Enter phone number in frontend (must be verified in Twilio trial).
2. Click "Send OTP".
3. Receive OTP SMS on your phone.
4. Enter OTP and verify.
5. Backend returns JWT token.

## GitHub Push
```bash
git init
git add .
git commit -m "Initial OTP Auth project"
git remote add origin <your-repo-url>
git push -u origin main
```

---
