const BASE_URL = 'http://localhost:8089/digitalassistant';

const getAuthHeaders = () => {
  const token = localStorage.getItem('jwtToken');
  return {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  };
};

export async function loginUser(userName, password) {
  const response = await fetch(`${BASE_URL}/user/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ userName, password }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Login failed');
  }

  return await response.text();
}

export async function createProject(projectDTO) {
  const response = await fetch(`${BASE_URL}/project/add`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify(projectDTO),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to create project');
  }

  return await response.json();
}

export async function addDocument(file, name, type, projectId) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('name', name);
  formData.append('type', type);
  formData.append('projectId', projectId);

  const response = await fetch(`${BASE_URL}/document/add`, {
    method: 'POST',
    body: formData,
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to add document');
  }

  return await response.json();
}

export async function setDocumentToProject(projectId, documentId) {
  const response = await fetch(`${BASE_URL}/document/setdocument?idp=${projectId}&idd=${documentId}`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('jwtToken')}`,
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to assign document to project');
  }

  return await response.json();
}

export async function assignProjectToUser(userId, projectId) {
  const response = await fetch(`${BASE_URL}/project/setproject?id=${userId}&idp=${projectId}`, {
    method: 'POST',
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to assign project to user');
  }

  return await response.json();
}

export async function fetchProjectsByUserId(userId) {
  const response = await fetch(`${BASE_URL}/user/getprojects?id=${userId}`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to fetch projects');
  }

  return await response.json();
}

export async function getProjectById(id) {
  const token = localStorage.getItem('token');
  const response = await fetch(`${BASE_URL}/project/get/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error('Failed to fetch project');
  }

  return await response.json();
}

export async function getDocumentsByProjectId(projectId) {
  const response = await fetch(`${BASE_URL}/document/getByProject?id=${projectId}`, {
    method: 'GET',
  });
  if (!response.ok) {
    throw new Error('Failed to fetch documents');
  }
  return await response.json();
}

export async function deleteProject(projectId) {
  const response = await fetch(`${BASE_URL}/project/delete/${projectId}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to delete project');
  }
}

export async function deleteDocument(documentId) {
  const response = await fetch(`${BASE_URL}/document/delete/${documentId}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to delete document');
  }
}
export async function generateReport(id, idprod) {
  const response = await fetch(
    `${BASE_URL}/project/generate?id=${id}&idprod=${idprod}`,
    {
      method: 'POST',
      headers: getAuthHeaders(),
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Failed to generate report');
  }

  return await response.text(); 
}

export async function getAllProduits() {
  const response = await fetch(`${BASE_URL}/produit/get/all`, {
    method: 'GET',
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    throw new Error('Failed to fetch produits');
  }

  return await response.json(); 
}