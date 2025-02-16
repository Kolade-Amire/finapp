(STILL UNDER DEVELOPMENT, SWITCH TO DEV BRANCHES TO SEE LATEST PUSHES)
A full-stack fintech application for loan management, transaction processing and user account management.

## Tech Stack

- Backend: Spring Boot 3.x with Java 17
- Frontend: Next.js 14 with TypeScript and Tailwind CSS
- Database: PostgreSQL
- Authentication: JWT

## Prerequisites

- Java 17
- Node.js 18+
- Docker and Docker Compose
- Maven

## Getting Started

1. Clone the repository:
   \`\`\`bash
   git clone [repository-url]
   cd loan-management-system
   \`\`\`

2. Start the application using Docker Compose:
   \`\`\`bash
   docker-compose up --build
   \`\`\`

3. Access the applications:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

## Development Setup

### Backend
\`\`\`bash
cd backend
./mvnw spring-boot:run
\`\`\`

### Frontend
\`\`\`bash
cd frontend
npm install
npm run dev
\`\`\`

## Testing

### Backend
\`\`\`bash
cd backend
./mvnw test
\`\`\`

### Frontend
\`\`\`bash
cd frontend
npm test
\`\`\`
