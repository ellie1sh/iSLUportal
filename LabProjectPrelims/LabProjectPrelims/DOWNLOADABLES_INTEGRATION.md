# Downloadables Integration Documentation

## Overview
This document explains the integration of the Spring Boot-inspired course management system into the ISLU Student Portal's Downloadables section.

## New Components Added

### Entity Package (`src/entity/`)
1. **Course.java** - Course entity with schedule information
2. **Student.java** - Student entity with enrollment details
3. **Enrollment.java** - Relationship entity between Student and Course
4. **StudentService.java** - Service layer for business logic

### Features Integrated

#### 1. Course Information Tab
- Displays all enrolled courses in a table format
- Shows: Class Code, Course Number, Description, Units, Schedule, Days, Room
- Calculates and displays total units
- Data sourced from the new entity system

#### 2. Student Data Tab
- Shows detailed student enrollment information
- Displays: Student ID, Name, Block, Academic Year, Semester, Total Units
- Uses the Student entity for data management

#### 3. About iSLU Tab
- University information and details
- Mission, Vision, Core Values
- Academic programs overview
- Campus life information

#### 4. System Information Tab
- Technical details about the application
- Version information
- Technology stack details
- Support information

## Sample Data
The system is initialized with sample data matching the original course schedule:

### Courses Included:
- NSTP-CWTS 1 (Foundations of Service) - 3 units
- GSTS (Science, Technology, and Society) - 3 units
- GENVI (Environmental Science) - 3 units
- CFE 103 (Catholic Foundation of Mission) - 3 units
- IT 211 (Requirements Analysis and Modeling) - 3 units
- IT 212 (Data Structures LEC) - 2 units
- IT 212L (Data Structures LAB) - 1 unit
- IT 213 (Network Fundamentals LEC) - 2 units
- IT 213L (Network Fundamentals LAB) - 1 unit
- FIT OA (Physical Activity) - 2 units

**Total Units: 23**

### Student Information:
- Student ID: STUDENT123
- Name: SHERLIE O. RIVERA
- Block: BSIT 2-3
- Academic Year: 2025-2026
- Semester: FIRST SEMESTER

## Code Structure

### Menu Integration
The Downloadables menu item is handled in the `showContent()` method switch statement:
```java
case "ℹ️ Downloadable/ About iSLU":
    contentPanel.add(createDownloadablesContent());
    break;
```

### Service Pattern
The `StudentService` class follows the singleton pattern and provides:
- Course data management
- Student information retrieval
- Table data formatting for JTable components

### Entity Relationships
- **Student** has many **Enrollments**
- **Course** can have many **Enrollments**
- **Enrollment** links **Student** and **Course**

## Usage
1. Run the ISLU Student Portal application
2. Navigate to the sidebar menu
3. Click on "ℹ️ Downloadable/ About iSLU"
4. Explore the four tabs:
   - Course Information (course schedule and details)
   - Student Data (enrollment information)
   - About iSLU (university information)
   - System Info (technical details)

## Technical Notes
- The code is adapted from Spring Boot entities to work with Java Swing
- No external dependencies required (uses standard Java libraries)
- Data is managed in-memory with sample initialization
- Compatible with the existing codebase structure
- Follows the same UI design patterns as other portal sections

## Future Enhancements
- Integration with actual database storage
- Dynamic course loading from external sources
- Student data persistence
- Export functionality for course information
- Print capabilities for schedules and transcripts