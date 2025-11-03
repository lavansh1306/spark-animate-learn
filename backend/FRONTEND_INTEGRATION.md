# Frontend Integration Guide

## Quick Start

1. **Start the backend server:**
```bash
cd backend
mvn spring-boot:run
```
Backend will run on `http://localhost:8080`

2. **Base URL for API calls:**
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

## Authentication Flow

### 1. Register a new user
```javascript
const register = async (name, email, password) => {
  const response = await fetch(`${API_BASE_URL}/auth/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ name, email, password }),
  });
  const data = await response.json();
  // Store token in localStorage
  localStorage.setItem('token', data.token);
  localStorage.setItem('user', JSON.stringify(data));
  return data;
};
```

### 2. Login
```javascript
const login = async (email, password) => {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });
  const data = await response.json();
  localStorage.setItem('token', data.token);
  localStorage.setItem('user', JSON.stringify(data));
  return data;
};
```

### 3. Get Auth Header
```javascript
const getAuthHeader = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };
};
```

## API Examples

### Get All Pages
```javascript
const getAllPages = async () => {
  const response = await fetch(`${API_BASE_URL}/pages`);
  return await response.json();
};
```

### Get Questions by Page
```javascript
const getQuestionsByPage = async (pageName, page = 0, size = 20) => {
  const response = await fetch(
    `${API_BASE_URL}/questions/page/name/${pageName}?page=${page}&size=${size}`
  );
  return await response.json();
};
```

### Create a Question (Requires Auth)
```javascript
const createQuestion = async (title, description, pageId) => {
  const response = await fetch(`${API_BASE_URL}/questions`, {
    method: 'POST',
    headers: getAuthHeader(),
    body: JSON.stringify({ title, description, pageId }),
  });
  return await response.json();
};
```

### Get Replies for a Question
```javascript
const getReplies = async (questionId) => {
  const response = await fetch(
    `${API_BASE_URL}/replies/question/${questionId}`
  );
  return await response.json();
};
```

### Add a Reply (Requires Auth)
```javascript
const addReply = async (questionId, content) => {
  const response = await fetch(
    `${API_BASE_URL}/replies/question/${questionId}`,
    {
      method: 'POST',
      headers: getAuthHeader(),
      body: JSON.stringify({ content }),
    }
  );
  return await response.json();
};
```

### Delete a Question (Requires Auth)
```javascript
const deleteQuestion = async (questionId) => {
  const response = await fetch(
    `${API_BASE_URL}/questions/${questionId}`,
    {
      method: 'DELETE',
      headers: getAuthHeader(),
    }
  );
  return await response.json();
};
```

## React Hook Example

```javascript
// hooks/useApi.js
import { useState, useEffect } from 'react';

const API_BASE_URL = 'http://localhost:8080/api';

export const usePages = () => {
  const [pages, setPages] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`${API_BASE_URL}/pages`)
      .then(res => res.json())
      .then(data => {
        setPages(data);
        setLoading(false);
      });
  }, []);

  return { pages, loading };
};

export const useQuestions = (pageName) => {
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (pageName) {
      fetch(`${API_BASE_URL}/questions/page/name/${pageName}`)
        .then(res => res.json())
        .then(data => {
          setQuestions(data);
          setLoading(false);
        });
    }
  }, [pageName]);

  return { questions, loading };
};
```

## Context API for Authentication

```javascript
// context/AuthContext.jsx
import { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    if (storedToken && storedUser) {
      setToken(storedToken);
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const login = async (email, password) => {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data));
    setToken(data.token);
    setUser(data);
    return data;
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
```

## Error Handling

```javascript
const handleApiCall = async (apiCall) => {
  try {
    const response = await apiCall();
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Something went wrong');
    }
    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw error;
  }
};
```

## Complete Example Component

```javascript
// components/QuestionList.jsx
import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

const QuestionList = ({ pageName }) => {
  const [questions, setQuestions] = useState([]);
  const { token } = useAuth();

  useEffect(() => {
    fetchQuestions();
  }, [pageName]);

  const fetchQuestions = async () => {
    const response = await fetch(
      `http://localhost:8080/api/questions/page/name/${pageName}`
    );
    const data = await response.json();
    setQuestions(data);
  };

  const handleDelete = async (questionId) => {
    await fetch(`http://localhost:8080/api/questions/${questionId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    fetchQuestions(); // Refresh list
  };

  return (
    <div>
      {questions.map(question => (
        <div key={question.id}>
          <h3>{question.title}</h3>
          <p>{question.description}</p>
          <small>By {question.userName} - {question.replyCount} replies</small>
          {token && <button onClick={() => handleDelete(question.id)}>Delete</button>}
        </div>
      ))}
    </div>
  );
};
```

## Next Steps

1. Update your frontend to use these API endpoints
2. Implement authentication flow with login/register
3. Create pages for different subjects (CSE, ECE, etc.)
4. Add forms for creating questions and replies
5. Handle loading states and errors properly
