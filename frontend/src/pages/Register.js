import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Register() {
  const [form, setForm] = useState({ name: '', email: '', password: '', monthlyBudget: '' });
  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      const res = await axios.post('http://localhost:8080/auth/register', form);
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('userId', 1);
      navigate('/dashboard');
    } catch (err) {
      alert('Registration failed!');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>Student Wallet AI</h1>
        <h3>Register</h3>
        <input placeholder="Name" onChange={e => setForm({...form, name: e.target.value})} />
        <input placeholder="Email" onChange={e => setForm({...form, email: e.target.value})} />
        <input placeholder="Password" type="password" onChange={e => setForm({...form, password: e.target.value})} />
        <input placeholder="Monthly Budget (₹)" onChange={e => setForm({...form, monthlyBudget: e.target.value})} />
        <button className="btn" onClick={handleRegister}>Register</button>
        <span className="link" onClick={() => navigate('/')}>Already have an account? Login</span>
      </div>
    </div>
  );
}

export default Register;