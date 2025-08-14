import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './login.css';
import { loginUser } from '../api/api.jsx';

export default function Login() {
  const navigate = useNavigate();
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const token = await loginUser(userName, password);
      localStorage.setItem('jwtToken', token);
      console.log('Login successful, token stored:', token);
      navigate('/dashboard');
    } catch (err) {
      console.error('Login failed:', err.message);
      setError('Login failed: ' + err.message);
    }
  };

  return (
    <main className="login-main">
      <div className="login-container">
        <h2>Login to ABC Digital Assistant</h2>
        <form onSubmit={handleSubmit}>
          <label htmlFor="userName">Username</label>
          <input
            type="text"
            id="userName"
            name="userName"
            placeholder="Your username"
            value={userName}
            onChange={(e) => setUserName(e.target.value)}
            required
          />
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            placeholder="Your password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">Login</button>
        </form>
        {error && <p className="login-error">{error}</p>}
      </div>
    </main>
  );
}
