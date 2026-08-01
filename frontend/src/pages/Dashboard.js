import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { PieChart, Pie, Cell, Tooltip, Legend } from 'recharts';

const COLORS = ['#6c63ff', '#ff6b6b', '#51cf66', '#ffd43b', '#74c0fc', '#f783ac'];

function Dashboard() {
  const [budget, setBudget] = useState(null);
  const [summary, setSummary] = useState([]);
  const userId = localStorage.getItem('userId') || 1;

  useEffect(() => {
    axios.get(`http://localhost:8080/budget/${userId}`).then(res => setBudget(res.data));
    axios.get('http://localhost:8080/transactions/summary').then(res => {
      const data = Object.entries(res.data).map(([name, value]) => ({ name, value }));
      setSummary(data);
    });
  }, [userId]);

  return (
    <div className="page">
      <h1>Dashboard</h1>

      {budget && (
        <div className="grid">
          <div className="card">
            <div className="stat-value">₹{budget.remaining}</div>
            <div className="stat-label">Remaining Budget</div>
          </div>
          <div className="card">
            <div className="stat-value">₹{budget.dailyLimit}</div>
            <div className="stat-label">Daily Limit</div>
          </div>
          <div className="card">
            <div className="stat-value">{budget.daysLeft}</div>
            <div className="stat-label">Days Left</div>
          </div>
          <div className="card">
            <div className="stat-value">₹{budget.totalSpent}</div>
            <div className="stat-label">Total Spent</div>
          </div>
        </div>
      )}

      {budget && (
        <div className="card">
          <p style={{fontSize: '18px'}}>{budget.message}</p>
        </div>
      )}

      {summary.length > 0 && (
        <div className="card">
          <h2>Spending by Category</h2>
          <PieChart width={400} height={300}>
            <Pie data={summary} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={100}>
              {summary.map((entry, index) => (
                <Cell key={index} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
            <Legend />
          </PieChart>
        </div>
      )}
    </div>
  );
}

export default Dashboard;