# Recruitment Management System - Backend

A comprehensive backend server for a Recruitment Management System built with Java Spring Boot.

## Features

- **User Authentication**: JWT-based authentication with role-based access control
- **Resume Processing**: Integration with third-party resume parser API
- **Job Management**: Admin can create and manage job openings
- **Application System**: Applicants can view and apply to jobs
- **Profile Management**: Automatic profile creation from resume data

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.3**
- **Spring Security** with JWT
- **Spring Data JPA**
- **H2 Database** (for development)
- **Maven** for dependency management

## Project Structure

```
src/main/java/com/recruitment/
├── controller/          # REST Controllers
├── dto/                # Data Transfer Objects
├── exception/          # Global Exception Handling
├── model/              # JPA Entities
├── repository/         # Data Access Layer
├── security/           # Security Configuration
└── service/            # Business Logic Layer
```

## API Endpoints

### Authentication
- `POST /signup` - Create user account
- `POST /login` - User login

### Resume Management (Applicant only)
- `POST /uploadResume` - Upload and process resume

### Job Management (Admin only)
- `POST /admin/job` - Create job opening
- `GET /admin/job/{jobId}` - Get job details with applicants
- `GET /admin/applicants` - Get all applicants
- `GET /admin/applicant/{applicantId}` - Get applicant details

### Job Applications (All users)
- `GET /jobs` - Get all job openings
- `GET /jobs/apply?job_id={jobId}` - Apply to job (Applicant only)
- `GET /jobs/my-applications` - Get user's applied jobs

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd recruitment-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8081`
   - H2 Console: `http://localhost:8081/h2-console`
     - JDBC URL: `jdbc:h2:mem:recruitmentdb`
     - Username: `sa`
     - Password: `password`

## API Usage Examples

### 1. User Registration
```bash
curl -X POST http://localhost:8081/signup \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "userType": "APPLICANT",
    "profileHeadline": "Software Developer",
    "address": "123 Main St, City, State"
  }'
```

### 2. User Login
```bash
curl -X POST http://localhost:8081/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Upload Resume (Applicant)
```bash
curl -X POST http://localhost:8081/uploadResume \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/resume.pdf"
```

### 4. Create Job (Admin)
```bash
curl -X POST http://localhost:8081/admin/job \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Senior Java Developer",
    "description": "Looking for experienced Java developer...",
    "companyName": "Tech Corp"
  }'
```

### 5. Apply to Job (Applicant)
```bash
curl -X GET "http://localhost:8081/jobs/apply?jobId=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Database Models

### User
- `id`: Primary key
- `name`: User's full name
- `email`: Unique email address
- `address`: User's address
- `userType`: ADMIN or APPLICANT
- `passwordHash`: Encrypted password
- `profileHeadline`: Professional headline

### Profile
- `id`: Primary key
- `applicant`: Reference to User
- `resumeFileAddress`: Path to uploaded resume
- `skills`: Extracted skills from resume
- `education`: Education information
- `experience`: Work experience
- `name`: Name from resume
- `email`: Email from resume
- `phone`: Phone number from resume

### Job
- `id`: Primary key
- `title`: Job title
- `description`: Job description
- `postedOn`: Creation timestamp
- `totalApplications`: Number of applications
- `companyName`: Company name
- `postedBy`: Admin who posted the job
- `applicants`: List of applicants

## Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Access Control**: Different permissions for Admin and Applicant
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Cross-origin resource sharing support

## Resume Processing

The system integrates with a third-party resume parser API to extract:
- Personal information (name, email, phone)
- Skills
- Education history
- Work experience

Supported file formats:
- PDF (.pdf)
- DOCX (.docx)

## Error Handling

The application includes comprehensive error handling for:
- Validation errors
- Authentication failures
- File upload errors
- Database errors
- API integration errors

## Development Notes

- Uses H2 in-memory database for development
- JWT tokens expire after 24 hours
- File upload limit: 10MB
- All API responses follow consistent JSON format

## Testing

You can test the API using:
- Postman
- curl commands
- Any REST client

## Production Considerations

For production deployment:
1. Replace H2 with PostgreSQL/MySQL
2. Configure proper JWT secrets
3. Set up file storage (AWS S3, etc.)
4. Configure proper logging
5. Set up monitoring and health checks
