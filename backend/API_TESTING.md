# API Testing Guide

Use these curl commands to test your backend API:

## 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Save the token from the response!

## 3. Get all pages
```bash
curl http://localhost:8080/api/pages
```

## 4. Get questions for CSE page
```bash
curl http://localhost:8080/api/questions/page/name/CSE
```

## 5. Create a question (replace YOUR_TOKEN)
```bash
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "How to implement linked list?",
    "description": "I need help understanding linked list implementation in Java",
    "pageId": "GET_PAGE_ID_FROM_PAGES_API"
  }'
```

## 6. Add a reply (replace YOUR_TOKEN and QUESTION_ID)
```bash
curl -X POST http://localhost:8080/api/replies/question/QUESTION_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "content": "Here is how you implement a linked list..."
  }'
```

## 7. Get replies for a question
```bash
curl http://localhost:8080/api/replies/question/QUESTION_ID
```

## Testing with Postman

1. Import these endpoints into Postman
2. Create an environment variable `baseUrl` = `http://localhost:8080/api`
3. Create an environment variable `token` to store JWT
4. Use `{{baseUrl}}` and `Bearer {{token}}` in your requests

## Expected Response Codes

- 200: Success
- 201: Created
- 400: Bad Request (validation errors)
- 401: Unauthorized (missing/invalid token)
- 404: Not Found
- 500: Internal Server Error
