import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './Dashboard.css';
import { getUserFromToken, getUserIdFromToken } from '../utils/jwtUtils';
import {
  createProject,
  assignProjectToUser,
  fetchProjectsByUserId,
} from '../api/api.jsx';

function getFrenchDate() {
  const now = new Date();
  return now.toLocaleDateString('fr-FR', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  });
}

export default function Dashboard() {
  const [projects, setProjects] = useState([]);
  const [userName, setUserName] = useState('');
  const [userId, setUserId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    createdAt: getFrenchDate(),
  });

  useEffect(() => {
    const name = getUserFromToken();
    const id = getUserIdFromToken();
    setUserName(name || 'Guest');
    setUserId(id);

    if (id) {
      fetchProjectsByUserId(id)
        .then((data) => {
          setProjects(data);
        })
        .catch((err) => {
          console.error('Error fetching projects:', err);
        });
    }
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const created = await createProject(formData);
      await assignProjectToUser(userId, created.id);
      setProjects((prev) => [...prev, created]);
      setFormData({ name: '', description: '', createdAt: getFrenchDate() });
      setShowForm(false);
    } catch (error) {
      console.error('Error creating project:', error);
    }
  };

  return (
    <>
      <div className="header-content">
        <h5>Welcome to your dashboard, {userName}!</h5>
      </div>

      <section className="cards-section">
        <h2>Your Projects</h2>
        <div className="card-grid">
          {projects.map((project) => (
            <div key={project.id} className="card">
              <h3>{project.name}</h3>
              <p>{project.description}</p>
              <p><strong>Created:</strong> {project.createdAt}</p>
              <br></br>
             <Link to={`/project/${project.id}`} className="manage-button">Manage</Link>

            </div>
          ))}
        </div>
        <div className="add-button-container">
          <button className="add-button" onClick={() => setShowForm(true)}>
            New project
          </button>
        </div>
        {showForm && (
          <div className="form-container">
            <h3 type="text">Create New Project</h3>
            <form onSubmit={handleFormSubmit} className="project-form">
              <label>
                Name:
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <label>
                Description:
                <textarea
                  name="description"
                  type="text"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <label>
                Created At:
                <input
                  type="text"
                  name="createdAt"
                  value={formData.createdAt}
                  onChange={handleInputChange}
                  required
                />
              </label>
              <div className="form-buttons">
                <button type="submit" className="manage-button">
                  Create
                </button>
                <button
                  type="button"
                  className="manage-button"
                  onClick={() => setShowForm(false)}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}
      </section>
    </>
  );
}
