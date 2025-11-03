# Integration Examples for Your Existing Frontend

Based on your frontend structure, here's how to integrate the backend:

## 1. Create API Service (`src/lib/api.ts`)

```typescript
const API_BASE_URL = 'http://localhost:8080/api';

// Helper to get auth headers
const getAuthHeaders = () => {
  const token = localStorage.getItem('token');
  return {
    'Content-Type': 'application/json',
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

// Auth API
export const authApi = {
  register: async (name: string, email: string, password: string) => {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password }),
    });
    if (!response.ok) throw new Error('Registration failed');
    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data));
    return data;
  },

  login: async (email: string, password: string) => {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    if (!response.ok) throw new Error('Login failed');
    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(data));
    return data;
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
};

// Pages API
export const pagesApi = {
  getAll: async () => {
    const response = await fetch(`${API_BASE_URL}/pages`);
    if (!response.ok) throw new Error('Failed to fetch pages');
    return response.json();
  },

  getByName: async (name: string) => {
    const response = await fetch(`${API_BASE_URL}/pages/name/${name}`);
    if (!response.ok) throw new Error('Page not found');
    return response.json();
  },
};

// Questions API
export const questionsApi = {
  getByPage: async (pageName: string, page = 0, size = 20) => {
    const response = await fetch(
      `${API_BASE_URL}/questions/page/name/${pageName}?page=${page}&size=${size}`
    );
    if (!response.ok) throw new Error('Failed to fetch questions');
    return response.json();
  },

  getById: async (id: string) => {
    const response = await fetch(`${API_BASE_URL}/questions/${id}`);
    if (!response.ok) throw new Error('Question not found');
    return response.json();
  },

  create: async (title: string, description: string, pageId: string) => {
    const response = await fetch(`${API_BASE_URL}/questions`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify({ title, description, pageId }),
    });
    if (!response.ok) throw new Error('Failed to create question');
    return response.json();
  },

  delete: async (id: string) => {
    const response = await fetch(`${API_BASE_URL}/questions/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to delete question');
    return response.json();
  },
};

// Replies API
export const repliesApi = {
  getByQuestion: async (questionId: string) => {
    const response = await fetch(
      `${API_BASE_URL}/replies/question/${questionId}`
    );
    if (!response.ok) throw new Error('Failed to fetch replies');
    return response.json();
  },

  create: async (questionId: string, content: string) => {
    const response = await fetch(
      `${API_BASE_URL}/replies/question/${questionId}`,
      {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({ content }),
      }
    );
    if (!response.ok) throw new Error('Failed to create reply');
    return response.json();
  },

  delete: async (id: string) => {
    const response = await fetch(`${API_BASE_URL}/replies/${id}`, {
      method: 'DELETE',
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to delete reply');
    return response.json();
  },
};
```

## 2. Update DoubtCard Component

```tsx
// src/components/DoubtCard.tsx
import { useState } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from './ui/card';
import { Button } from './ui/button';
import { questionsApi, repliesApi } from '@/lib/api';

interface DoubtCardProps {
  question: {
    id: string;
    title: string;
    description: string;
    userName: string;
    replyCount: number;
    createdAt: string;
  };
  onDelete?: () => void;
}

export const DoubtCard = ({ question, onDelete }: DoubtCardProps) => {
  const [showReplies, setShowReplies] = useState(false);
  const [replies, setReplies] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleShowReplies = async () => {
    if (!showReplies) {
      setLoading(true);
      try {
        const data = await repliesApi.getByQuestion(question.id);
        setReplies(data);
      } catch (error) {
        console.error('Failed to load replies:', error);
      }
      setLoading(false);
    }
    setShowReplies(!showReplies);
  };

  return (
    <Card className="mb-4">
      <CardHeader>
        <CardTitle>{question.title}</CardTitle>
      </CardHeader>
      <CardContent>
        <p className="text-gray-700 mb-2">{question.description}</p>
        <div className="flex justify-between items-center text-sm text-gray-500">
          <span>Posted by: {question.userName}</span>
          <span>{question.replyCount} replies</span>
        </div>
        <Button onClick={handleShowReplies} className="mt-2">
          {showReplies ? 'Hide Replies' : 'Show Replies'}
        </Button>
        
        {showReplies && (
          <div className="mt-4">
            {loading ? (
              <p>Loading replies...</p>
            ) : (
              replies.map((reply: any) => (
                <div key={reply.id} className="border-l-2 pl-4 mt-2">
                  <p>{reply.content}</p>
                  <small className="text-gray-500">
                    By {reply.userName} on {new Date(reply.createdAt).toLocaleDateString()}
                  </small>
                </div>
              ))
            )}
          </div>
        )}
      </CardContent>
    </Card>
  );
};
```

## 3. Create a Page Component for CSE/ECE/Math etc.

```tsx
// src/pages/SubjectPage.tsx
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { DoubtCard } from '@/components/DoubtCard';
import { PostButton } from '@/components/PostButton';
import { questionsApi, pagesApi } from '@/lib/api';

export const SubjectPage = () => {
  const { pageName } = useParams(); // e.g., "CSE", "ECE"
  const [questions, setQuestions] = useState([]);
  const [page, setPage] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, [pageName]);

  const loadData = async () => {
    try {
      const [pageData, questionsData] = await Promise.all([
        pagesApi.getByName(pageName),
        questionsApi.getByPage(pageName),
      ]);
      setPage(pageData);
      setQuestions(questionsData);
    } catch (error) {
      console.error('Failed to load data:', error);
    }
    setLoading(false);
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-2">{page?.name}</h1>
      <p className="text-gray-600 mb-6">{page?.description}</p>
      
      <PostButton pageId={page?.id} onPostSuccess={loadData} />
      
      <div className="mt-6">
        {questions.map((question) => (
          <DoubtCard key={question.id} question={question} />
        ))}
      </div>
    </div>
  );
};
```

## 4. Update CommunitySidebar with Real Pages

```tsx
// src/components/CommunitySidebar.tsx
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { pagesApi } from '@/lib/api';

export const CommunitySidebar = () => {
  const [pages, setPages] = useState([]);

  useEffect(() => {
    pagesApi.getAll().then(setPages);
  }, []);

  return (
    <aside className="w-64 bg-white shadow-md p-4">
      <h2 className="text-xl font-bold mb-4">Subjects</h2>
      <nav>
        {pages.map((page: any) => (
          <Link
            key={page.id}
            to={`/page/${page.name}`}
            className="block py-2 px-4 hover:bg-gray-100 rounded"
          >
            {page.name}
            <span className="text-sm text-gray-500 ml-2">
              ({page.questionCount})
            </span>
          </Link>
        ))}
      </nav>
    </aside>
  );
};
```

## 5. Update PostButton for Creating Questions

```tsx
// src/components/PostButton.tsx
import { useState } from 'react';
import { Button } from './ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from './ui/dialog';
import { Input } from './ui/input';
import { Textarea } from './ui/textarea';
import { questionsApi, authApi } from '@/lib/api';

interface PostButtonProps {
  pageId: string;
  onPostSuccess: () => void;
}

export const PostButton = ({ pageId, onPostSuccess }: PostButtonProps) => {
  const [open, setOpen] = useState(false);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const user = authApi.getCurrentUser();
    if (!user) {
      alert('Please login to post a question');
      return;
    }

    setLoading(true);
    try {
      await questionsApi.create(title, description, pageId);
      setTitle('');
      setDescription('');
      setOpen(false);
      onPostSuccess();
    } catch (error) {
      alert('Failed to post question');
    }
    setLoading(false);
  };

  return (
    <>
      <Button onClick={() => setOpen(true)}>Post a Question</Button>
      
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Post Your Question</DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              placeholder="Question Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
            <Textarea
              placeholder="Describe your question in detail..."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
              rows={5}
            />
            <Button type="submit" disabled={loading}>
              {loading ? 'Posting...' : 'Post Question'}
            </Button>
          </form>
        </DialogContent>
      </Dialog>
    </>
  );
};
```

## 6. Add Authentication Pages

```tsx
// src/pages/Login.tsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { authApi } from '@/lib/api';

export const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await authApi.login(email, password);
      navigate('/');
    } catch (error) {
      alert('Login failed');
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen">
      <Card className="w-96">
        <CardHeader>
          <CardTitle>Login to Spark</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <Input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <Button type="submit" className="w-full">Login</Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};
```

## 7. Update Router (in App.tsx or main routing file)

```tsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Index } from './pages/Index';
import { SubjectPage } from './pages/SubjectPage';
import { Login } from './pages/Login';
import { Header } from './components/Header';

function App() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Index />} />
        <Route path="/page/:pageName" element={<SubjectPage />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}
```

## Quick Setup Checklist

1. ✅ Backend is running on `http://localhost:8080`
2. ✅ Create `src/lib/api.ts` with all API functions
3. ✅ Update components to use real API calls
4. ✅ Add authentication pages (Login/Register)
5. ✅ Update routing to support subject pages
6. ✅ Test login → post question → add reply flow

## Testing Flow

1. Start backend: `cd backend && ./run.sh`
2. Start frontend: `npm run dev`
3. Register a new user
4. Browse CSE page
5. Post a question
6. Add a reply
7. See it all work together! 🎉
