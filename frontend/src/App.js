import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Transactions from './pages/Transactions';
import Predictions from './pages/Predictions';
import Navbar from './components/Navbar';
import './App.css';

function App() {
  const token = localStorage.getItem('token');

  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={token ? <><Navbar /><Dashboard /></> : <Navigate to="/" />} />
          <Route path="/transactions" element={token ? <><Navbar /><Transactions /></> : <Navigate to="/" />} />
          <Route path="/predictions" element={token ? <><Navbar /><Predictions /></> : <Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;