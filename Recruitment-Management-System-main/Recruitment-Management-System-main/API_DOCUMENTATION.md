# Recruitment Management System - API Documentation

## Base URL
```
http://localhost:8081
```

## Authentication
All protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 1. Authentication Endpoints

### POST /signup
Create a new user account.

**Request Body:**
```json
{
  "name": "string",
  "email": "string",
  "password": "string",
  "userType": "ADMIN" | "APPLICANT",
  "profileHeadline": "string",
  "address": "string"
}
```

**Response:**
```json
{
  "token": "string",
  "type": "Bearer",
  "userId": "number",
  "email": "string",
  "name": "string",
  "userType": "string"
}
```

### POST /login
Authenticate user and get JWT token.

**Request Body:**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "string",
  "type": "Bearer",
  "userId": "number",
  "email": "string",
  "name": "string",
  "userType": "string"
}
```

---

## 2. Resume Management (Applicant Only)

### POST /uploadResume
Upload and process resume file.

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`
- `Content-Type: multipart/form-data`

**Request:**
- `file`: Resume file (PDF or DOCX only)

**Response:**
```json
{
  "message": "Resume uploaded and processed successfully",
  "profileId": "number",
  "name": "string",
  "email": "string",
  "phone": "string",
  "skills": "string",
  "education": "string",
  "experience": "string"
}
```

---

## 3. Admin Endpoints

### POST /admin/job
Create a new job opening. (Admin only)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`
- `Content-Type: application/json`

**Request Body:**
```json
{
  "title": "string",
  "description": "string",
  "companyName": "string"
}
```

**Response:**
```json
{
  "message": "Job created successfully",
  "jobId": "number",
  "title": "string",
  "companyName": "string",
  "postedOn": "datetime"
}
```

### GET /admin/job/{jobId}
Get job details with list of applicants. (Admin only)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`

**Response:**
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "companyName": "string",
  "postedOn": "datetime",
  "totalApplications": "number",
  "postedBy": "string",
  "applicants": [
    {
      "id": "number",
      "name": "string",
      "email": "string",
      "profileHeadline": "string"
    }
  ]
}
```

### GET /admin/applicants
Get list of all applicants. (Admin only)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`

**Response:**
```json
[
  {
    "id": "number",
    "name": "string",
    "email": "string",
    "address": "string",
    "profileHeadline": "string",
    "userType": "string"
  }
]
```

### GET /admin/applicant/{applicantId}
Get detailed information about a specific applicant. (Admin only)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`

**Response:**
```json
{
  "applicant": {
    "id": "number",
    "name": "string",
    "email": "string",
    "address": "string",
    "profileHeadline": "string"
  },
  "profile": {
    "id": "number",
    "name": "string",
    "email": "string",
    "phone": "string",
    "skills": "string",
    "education": "string",
    "experience": "string",
    "resumeFileAddress": "string"
  }
}
```

---

## 4. Job Management Endpoints

### GET /jobs
Get all available job openings. (All authenticated users)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`

**Response:**
```json
[
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "companyName": "string",
    "postedOn": "datetime",
    "totalApplications": "number",
    "postedBy": "string"
  }
]
```

### GET /jobs/apply?jobId={jobId}
Apply to a specific job. (Applicant only)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`

**Response:**
```json
{
  "message": "Successfully applied to job",
  "jobId": "number",
  "jobTitle": "string",
  "companyName": "string",
  "totalApplications": "number"
}
```

### GET /jobs/my-applications
Get list of jobs the user has applied to. (All authenticated users)

**Headers:**
- `Authorization: Bearer YOUR_JWT_TOKEN`

**Response:**
```json
[
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "companyName": "string",
    "postedOn": "datetime",
    "totalApplications": "number",
    "postedBy": "string"
  }
]
```

---

## Error Responses

All endpoints may return error responses in the following format:

```json
{
  "error": "Error message description"
}
```

### Common HTTP Status Codes:
- `200 OK` - Request successful
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Validation Errors:
For validation errors, the response includes field-specific errors:

```json
{
  "fieldName": "Error message for this field",
  "anotherField": "Error message for another field"
}
```

---

## Example Usage Flow

### 1. Register as Applicant
```bash
curl -X POST http://localhost:8080/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "password": "password123",
    "userType": "APPLICANT",
    "profileHeadline": "Software Engineer",
    "address": "456 Oak St, City, State"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jane@example.com",
    "password": "password123"
  }'
```

### 3. Upload Resume
```bash
curl -X POST http://localhost:8080/uploadResume \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@resume.pdf"
```

### 4. View Available Jobs
```bash
curl -X GET http://localhost:8080/jobs \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Apply to Job
```bash
curl -X GET "http://localhost:8080/jobs/apply?jobId=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Notes

- JWT tokens expire after 24 hours
- File uploads are limited to 10MB
- Only PDF and DOCX files are accepted for resume uploads
- All timestamps are in ISO 8601 format
- The system automatically extracts information from uploaded resumes using a third-party API
