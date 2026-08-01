import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const res = await axios.post('http://localhost:8080/auth/login', { email, password });
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('userId', 1);
      navigate('/dashboard');
    } catch (err) {
      setError('Invalid credentials!');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-box">
        <h1>Student Wallet AI</h1>
        <h3>Login</h3>
        {error && <p className="warning">{error}</p>}
        <input placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} />
        <input placeholder="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} />
        <button className="btn" onClick={handleLogin}>Login</button>
        <span className="link" onClick={() => navigate('/register')}>Don't have an account? Register</span>
      </div>
    </div>
  );
}

export default Login;