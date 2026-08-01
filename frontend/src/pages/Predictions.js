import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Predictions() {
  const [budget, setBudget] = useState(null);
  const [brokeDate, setBrokeDate] = useState(null);
  const userId = localStorage.getItem('userId') || 1;

  useEffect(() => {
    axios.get(`http://localhost:8080/budget/${userId}`).then(async res => {
      setBudget(res.data);
      const dailyAverage = res.data.totalSpent / (30 - res.data.daysLeft || 1);
      const broke = await axios.post('http://localhost:5000/predict/brokedate', {
        dailyAverage,
        remaining: res.data.remaining,
        daysLeft: res.data.daysLeft
      });
      setBrokeDate(broke.data);
    });
  }, [userId]);

  return (
    <div className="page">
      <h1>Predictions</h1>

      {budget && (
        <div className="grid">
          <div className="card">
            <h2>Budget Status</h2>
            <div className="stat-value">₹{budget.remaining}</div>
            <div className="stat-label">Remaining</div>
            <p style={{marginTop: '16px'}}>{budget.message}</p>
          </div>

          <div className="card">
            <h2>Daily Spending Limit</h2>
            <div className="stat-value">₹{budget.dailyLimit}</div>
            <div className="stat-label">Per day for {budget.daysLeft} days</div>
          </div>
        </div>
      )}

      {brokeDate && (
        <div className="card">
          <h2>Broke Date Prediction 🔮</h2>
          <p className={brokeDate.predictedBrokeDay >= 28 ? 'success' : 'warning'} style={{fontSize: '18px', marginTop: '12px'}}>
            {brokeDate.message}
          </p>
        </div>
      )}
    </div>
  );
}

export default Predictions;