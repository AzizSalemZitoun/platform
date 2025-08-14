import { Link } from 'react-router-dom'
import './Landing.css'  
export default function Landing() {
  return (
  
    <>
      <header className="landing-header">
        <div className="header-content">
          <h1>Welcome to ABC digital assistant</h1>
          <p>
            Simplfy your client handling process with ABC's very own digital assistant using Artificial intelligence.<br></br>
             Upload documents, organize projects and optimize your workflow.<br></br><br></br>
             <Link to="/dashboard" className="login-link">Access your dashboard</Link>
          </p>
        </div>
        <div className="header-image">
          <img
            src="logo.png"
            alt="ABC digital assistant Illustration"
          />
        </div>
      </header>
      <section className="features-section">
        <h2>How does ABC digital assistant work ?</h2>
        <div className="features-grid">
          <div className="feature-card">
            <h3>Organize Projects</h3>
            <p>Create and manage projects to keep them well structured.</p>
          </div>
          <div className="feature-card">
            <h3>Easy Document Upload</h3>
            <p>Upload your client documents for instant processing in one click .</p>
          </div>
          
          <div className="feature-card">
            <h3>Handle clients easily</h3>
            <p>Automatically generate partially completed reports.</p>
          </div>
        </div>
      </section>
     
    </>
  )
}
