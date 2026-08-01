import React from 'react';
import { useNavigate } from 'react-router-dom';

function Navbar() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.clear();
    navigate('/');
  };

  return (
    <nav style={{
      background: '#1a1a2e',
      padding: '16px 30px',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center',
      borderBottom: '1px solid #2a2a4a'
    }}>
      <h2 style={{color: '#6c63ff', cursor: 'pointer'}} onClick={() => navigate('/dashboard')}>
        💰 Student Wallet AI
      </h2>
      <div style={{display: 'flex', gap: '20px'}}>
        <button onClick={() => navigate('/dashboard')} style={{background: 'none', border: 'none', color: 'white', cursor: 'pointer', fontSize: '16px'}}>Dashboard</button>
        <button onClick={() => navigate('/transactions')} style={{background: 'none', border: 'none', color: 'white', cursor: 'pointer', fontSize: '16px'}}>Transactions</button>
        <button onClick={() => navigate('/predictions')} style={{background: 'none', border: 'none', color: 'white', cursor: 'pointer', fontSize: '16px'}}>Predictions</button>
        <button onClick={logout} style={{background: '#ff6b6b', border: 'none', color: 'white', cursor: 'pointer', fontSize: '16px', padding: '8px 16px', borderRadius: '8px'}}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;