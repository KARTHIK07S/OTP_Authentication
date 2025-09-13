import React, { useState } from "react";
import "./App.css";

function App() {
  const [phone, setPhone] = useState("");
  const [otp, setOtp] = useState("");
  const [status, setStatus] = useState("");

  // If your backend uses a different port change 8080 -> 9090 here
  const API_BASE = "http://localhost:8080/api/auth";

  const sendOtp = async () => {
    if (!phone) { setStatus("Enter a phone number"); return; }
    setStatus("Sending OTP...");
    try {
      const res = await fetch(`${API_BASE}/send-otp`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phone })
      });
      const data = await res.json();
      setStatus(data?.message || "No response message");
    } catch (err) {
      console.error(err);
      setStatus("Error sending OTP (check backend).");
    }
  };

  const verifyOtp = async () => {
    if (!otp) { setStatus("Enter the OTP"); return; }
    setStatus("Verifying...");
    try {
      const res = await fetch(`${API_BASE}/verify-otp`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ phone, otp })
      });
      const data = await res.json();
      setStatus(data?.message || "No response message");
    } catch (err) {
      console.error(err);
      setStatus("Error verifying OTP (check backend).");
    }
  };

  return (
    <div className="container">
      <h2>OTP Authentication</h2>
      <input
        type="text"
        placeholder="Enter phone number (+91xxxx)"
        value={phone}
        onChange={(e) => setPhone(e.target.value)}
      />
      <button onClick={sendOtp}>Send OTP</button>

      <input
        type="text"
        placeholder="Enter OTP"
        value={otp}
        onChange={(e) => setOtp(e.target.value)}
      />
      <button onClick={verifyOtp}>Verify OTP</button>

      <p id="status">{status}</p>
    </div>
  );
}

export default App;




