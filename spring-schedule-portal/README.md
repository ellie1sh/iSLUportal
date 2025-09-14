# iSLU Student Portal - Spring Boot Backend

This is the Spring Boot backend service for the iSLU Student Portal, providing REST APIs for student schedule management.

## Features

- **Student Management**: CRUD operations for student data
- **Course Management**: Course scheduling and information
- **Enrollment System**: Student-course enrollment relationships
- **Schedule API**: RESTful endpoints for schedule data
- **H2 Database**: In-memory database for development
- **Data Initialization**: Automatic sample data loading

## Project Structure

```
spring-schedule-portal/
├── src/main/java/com/islu/portal/
│   ├── entity/          # JPA Entity classes
│   │   ├── Course.java
│   │   ├── Student.java
│   │   └── Enrollment.java
│   ├── repository/      # Data Access Layer
│   │   ├── CourseRepository.java
│   │   ├── StudentRepository.java
│   │   └── EnrollmentRepository.java
│   ├── service/         # Business Logic Layer
│   │   └── StudentService.java
│   ├── controller/      # REST Controllers
│   │   └── ScheduleController.java
│   ├── config/          # Configuration Classes
│   │   └── DataInitializer.java
│   └── StudentPortalApplication.java  # Main Application
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+

### Running the Application

1. **Navigate to the project directory:**
   ```bash
   cd /workspace/spring-schedule-portal
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application:**
   - Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: `password`

## API Endpoints

### Schedule Management

- **GET** `/students/schedule/{studentId}` - Get student schedule (HTML view)
- **GET** `/students/api/schedule/{studentId}` - Get student schedule (JSON API)

### Sample API Response

```json
{
  "student": {
    "studentId": "STUDENT123",
    "fullName": "SHERLIE O. RIVERA",
    "block": "BSIT 2-3",
    "academicYear": "2025-2026",
    "semester": "FIRST SEMESTER"
  },
  "courses": [
    {
      "classCode": "7024",
      "courseNumber": "NSTP-CWTS 1",
      "courseDescription": "FOUNDATIONS OF SERVICE",
      "units": 3,
      "startTime": "13:30:00",
      "endTime": "14:30:00",
      "days": "MWF",
      "room": "D906"
    }
  ],
  "totalUnits": 23
}
```

## Database Schema

### Tables

1. **students**
   - id (Primary Key)
   - student_id (Unique)
   - full_name
   - block
   - academic_year
   - semester

2. **courses**
   - id (Primary Key)
   - class_code
   - course_number
   - course_description
   - units
   - start_time
   - end_time
   - days
   - room

3. **enrollments**
   - id (Primary Key)
   - student_id (Foreign Key)
   - course_id (Foreign Key)

## Integration with Swing Application

The Spring Boot backend is designed to integrate with the existing Java Swing portal through:

1. **SpringScheduleIntegration.java** - Bridge class that connects Swing UI with backend data
2. **REST API calls** - HTTP requests to fetch schedule data
3. **Fallback mechanism** - Uses local data if backend is unavailable

### Testing Integration

1. Start the Spring Boot application
2. Run the Java Swing portal
3. Navigate to the Schedule section
4. The enhanced schedule will automatically load if the backend is available

## Sample Data

The application automatically initializes with sample data for student "STUDENT123" including:

- 10 courses across different subjects
- Proper time scheduling (MWF, TTHS, TF patterns)
- Room assignments
- Unit calculations

## Development

### Adding New Features

1. **Entity Changes**: Modify JPA entities in `entity/` package
2. **Database Operations**: Add methods to repository interfaces
3. **Business Logic**: Implement in service classes
4. **API Endpoints**: Create controllers for new endpoints

### Configuration

- **Database**: Modify `application.properties` for different database settings
- **Port**: Change `server.port` property
- **JPA**: Adjust hibernate settings as needed

## Troubleshooting

### Common Issues

1. **Port 8080 already in use**: Change port in `application.properties`
2. **Database connection**: Check H2 console for data verification
3. **API not responding**: Verify application startup logs

### Logs

Check application logs for detailed error information:
```bash
mvn spring-boot:run
```

## Next Steps

1. **Database Migration**: Move from H2 to production database (MySQL/PostgreSQL)
2. **Authentication**: Add Spring Security for user authentication
3. **Frontend**: Create proper web frontend (React/Angular)
4. **Testing**: Add unit and integration tests
5. **Deployment**: Configure for production deployment

## Support

For issues and questions, check the application logs and verify that all dependencies are properly installed.