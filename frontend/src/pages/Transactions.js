import React, { useState, useEffect } from 'react';
import axios from 'axios';

function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [form, setForm] = useState({ amount: '', merchant: '', category: '', description: '' });

  const fetchTransactions = () => {
    axios.get('http://localhost:8080/transactions').then(res => setTransactions(res.data));
  };

  useEffect(() => { fetchTransactions(); }, []);

  const handleAdd = async () => {
    await axios.post('http://localhost:8080/transactions', form);
    fetchTransactions();
    setForm({ amount: '', merchant: '', category: '', description: '' });
  };

  const handleDelete = async (id) => {
    await axios.delete(`http://localhost:8080/transactions/${id}`);
    fetchTransactions();
  };

  const guiltEmoji = (score) => {
    if (score <= 2) return '✅';
    if (score <= 5) return '😐';
    if (score <= 7) return '😬';
    return '💀';
  };

  return (
    <div className="page">
      <h1>Transactions</h1>

      <div className="card">
        <h2>Add Transaction</h2>
        <input placeholder="Amount (₹)" value={form.amount} onChange={e => setForm({...form, amount: e.target.value})} />
        <input placeholder="Merchant (e.g. Swiggy)" value={form.merchant} onChange={e => setForm({...form, merchant: e.target.value})} />
        <input placeholder="Category" value={form.category} onChange={e => setForm({...form, category: e.target.value})} />
        <input placeholder="Description" value={form.description} onChange={e => setForm({...form, description: e.target.value})} />
        <button className="btn" onClick={handleAdd}>Add Transaction</button>
      </div>

      <div className="card">
        <h2>All Transactions</h2>
        <table>
          <thead>
            <tr>
              <th>Merchant</th>
              <th>Amount</th>
              <th>Category</th>
              <th>Date</th>
              <th>Guilt</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map(t => (
              <tr key={t.id}>
                <td>{t.merchant}</td>
                <td>₹{t.amount}</td>
                <td>{t.category}</td>
                <td>{new Date(t.date).toLocaleDateString()}</td>
                <td>{guiltEmoji(t.guiltScore)}</td>
                <td><button onClick={() => handleDelete(t.id)} style={{color: 'red', background: 'none', border: 'none', cursor: 'pointer'}}>Delete</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default Transactions;