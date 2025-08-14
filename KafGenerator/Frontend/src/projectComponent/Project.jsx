import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { 
  getProjectById, 
  addDocument, 
  deleteDocument, 
  getDocumentsByProjectId,
  deleteProject 
} from '../api/api';
import './Project.css';

  

export default function Project() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [docType, setDocType] = useState('pdf');
  const [documentName, setDocumentName] = useState('');

  useEffect(() => {
    const loadProject = async () => {
      try {
        const data = await getProjectById(id);
        setProject(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    loadProject();
  }, [id]);

  const fetchDocuments = async () => {
    try {
      setDocuments(await getDocumentsByProjectId(id));
    } catch (err) {
      console.error('Failed to fetch documents:', err.message);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, [id]);

  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
    setDocumentName(e.target.files[0]?.name || '');
    setDocType(e.files[0]?.docType)
  };

  const handleAddDocument = async () => {
    if (!selectedFile || !docType || !documentName) {
      alert('Please select a file, type, and enter a document name.');
      return;
    }
    try{
  await addDocument(selectedFile, documentName, docType, id);
    await fetchDocuments()}
    catch (e){
      alert('Unsupported media type');
      return;
    }

  };

  const handleDeleteProject = async () => {
    if (window.confirm('Are you sure you want to delete this project?')) {
      await deleteProject(id);
      navigate('/dashboard');
    }
  };

  const handleDeleteDocument = async (docId) => {
    if (window.confirm('Are you sure you want to delete this document?')) {
      await deleteDocument(docId);
      await fetchDocuments();
    }
  };
 

  if (loading) return <div className="project-page">Loading...</div>;
  if (error) return <div className="project-page">Error: {error}</div>;
  if (!project) return <div className="project-page">No project found</div>;

  return (
    
    <div className="project-page">
      <header className="project-header">
       <Link to="/dashboard" className="back-link">
          ← Back to Dashboard
        </Link><br></br>
        <h1>{project.name}</h1>
        <p className="project-meta">
          Created on: <strong>{project.createdAt}</strong><br />
        </p>
        <p className="project-description">
          Description: <strong>{project.description}</strong>
        </p>
      </header>

      <section className="project-documents">
        <div className="documents-header">
          <h2>Documents</h2>
          <div className="upload-form">
           
            <input type="file" onChange={handleFileChange} />
            <button className="add-document-button" onClick={handleAddDocument}>
              Save
            </button>
          </div>
        </div>
      </section>

      <section className="project-linked-documents">
        <ul>
          {documents.length > 0 ? (
            documents.map((docs) => (
              <li  key={docs.id}>
                📄 {docs.name}
                <button className="delete" onClick={() => handleDeleteDocument(docs.id)}>🗑️</button>
              </li>
            ))
          ) : (
            <li>No documents 📄 found</li>
          )}
        </ul>
      </section>

      <section className="Caf-section">
        <h2>Decision Document</h2>
        <p>No decision document has been generated for this project yet.</p>
        <button className="generate-Caf-button">Start Analysis & Generate report</button>
      </section>

      <footer className="project-footer">
        <div className="button-group">
          <button className="btn btn-delete" onClick={handleDeleteProject}>Delete</button>
          <button className="btn btn-download">Download report</button>
        </div>
       
      </footer>
    </div>
  );
}