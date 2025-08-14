import { Link } from 'react-router-dom';
import './Navbar.css';

export default function Navbar() {
  return (
    <nav className="navbar">
      <div className="nav-container">
        <Link to="/" className="nav-logo">
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            <img src="logo.png" alt="Logo" />
            ABC digital assistant
          </div>
        </Link>
      </div>
    </nav>
  );
  
}
