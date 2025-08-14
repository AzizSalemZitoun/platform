import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './navbarComponent/NavBar.jsx'
import Landing from './Landing.jsx'
import Login from './loginComponent/login'
import Dashboard from './dashboardComponent/Dashboard.jsx'
import Project from './projectComponent/Project.jsx'
import Footer from './footerComponent/footer.jsx'

export default function App() {
  return (
    <>
    <Router>
      <Navbar></Navbar>
      <Routes>
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<Login />} />
        <Route path="/project/:id" element={<Project />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
            <Footer></Footer>

    </Router>
 </> )
  
}
