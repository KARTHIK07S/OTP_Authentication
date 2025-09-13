import React, { useState } from "react";
import 'react-phone-input-2/lib/style.css';
import PhoneInput from 'react-phone-input-2';

function App() {
  const [phone, setPhone] = useState("");
  const [step, setStep] = useState("phone");
  const [message, setMessage] = useState("");
  const [otp, setOtp] = useState("");
  const [token, setToken] = useState("");

  const API_BASE = "http://localhost:8080/api/auth";

  async function sendOtp() {
    setMessage("Sending OTP...");
    try {
      const res = await fetch(API_BASE + "/send-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phone: "+" + phone }) // Ensure proper format
      });
      const data = await res.json();
      if (res.ok) {
        setMessage("OTP sent. Please check your phone.");
        setStep("otp");
      } else {
        setMessage(data?.message || "Failed to send OTP");
      }
    } catch (err) {
      setMessage("Network error: " + err.message);
    }
  }

  async function verifyOtp() {
    setMessage("Verifying OTP...");
    try {
      const res = await fetch(API_BASE + "/verify-otp", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phone: "+" + phone, otp })
      });
      const data = await res.json();
      if (res.ok) {
        setMessage("OTP verified. Logged in!");
        setToken(data.token);
        setStep("done");
      } else {
        setMessage(data?.message || "Invalid OTP");
      }
    } catch (err) {
      setMessage("Network error: " + err.message);
    }
  }

  return (
    <div style={{ maxWidth: 420, margin: "50px auto", fontFamily: "Arial, sans-serif" }}>
      <h2>OTP Login</h2>

      {step === "phone" && (
        <>
          <label>Phone Number:</label>
          <PhoneInput
            country={'in'}
            value={phone}
            onChange={setPhone}
            inputStyle={{ width: "100%" }}
          />
          <button
            onClick={sendOtp}
            style={{ padding: "8px 12px", marginTop: "10px" }}
          >
            Send OTP
          </button>
        </>
      )}

      {step === "otp" && (
        <>
          <div>OTP sent to <b>+{phone}</b></div>
          <label>Enter OTP:</label>
          <input
            type="text"
            value={otp}
            onChange={e => setOtp(e.target.value)}
            placeholder="123456"
            style={{ width: "100%", padding: "8px", marginTop: "6px", marginBottom: "10px" }}
          />
          <button onClick={verifyOtp} style={{ padding: "8px 12px", marginRight: 8 }}>Verify OTP</button>
          <button onClick={() => setStep("phone")} style={{ padding: "8px 12px" }}>Change Phone</button>
        </>
      )}

      {step === "done" && (
        <>
          <div style={{ marginBottom: 10 }}>Logged in! 🎉 JWT Token:</div>
          <textarea readOnly value={token} style={{ width: "100%", minHeight: 60 }} />
        </>
      )}

      <div style={{ marginTop: 12, color: "#333" }}>{message}</div>
    </div>
  );
}

export default App;


