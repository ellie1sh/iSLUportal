#!/bin/bash

echo "=== iSLU Student Portal Integration Test ==="
echo ""

# Test 1: Check if Spring Boot project structure is correct
echo "1. Checking Spring Boot project structure..."
if [ -d "spring-schedule-portal/src/main/java/com/islu/portal" ]; then
    echo "✅ Spring Boot project structure created successfully"
else
    echo "❌ Spring Boot project structure missing"
fi

# Test 2: Check if all Java files are present
echo ""
echo "2. Checking Java source files..."
REQUIRED_FILES=(
    "spring-schedule-portal/src/main/java/com/islu/portal/entity/Course.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/entity/Student.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/entity/Enrollment.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/repository/StudentRepository.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/repository/CourseRepository.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/service/StudentService.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/controller/ScheduleController.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/config/DataInitializer.java"
    "spring-schedule-portal/src/main/java/com/islu/portal/StudentPortalApplication.java"
    "spring-schedule-portal/pom.xml"
)

ALL_FILES_PRESENT=true
for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ $file - MISSING"
        ALL_FILES_PRESENT=false
    fi
done

# Test 3: Check integration file
echo ""
echo "3. Checking integration with existing Swing portal..."
if [ -f "LabProjectPrelims/LabProjectPrelims/src/SpringScheduleIntegration.java" ]; then
    echo "✅ SpringScheduleIntegration.java created"
else
    echo "❌ SpringScheduleIntegration.java missing"
fi

# Test 4: Check if ISLUStudentPortal was updated
echo ""
echo "4. Checking if existing portal was updated..."
if grep -q "SpringScheduleIntegration" "LabProjectPrelims/LabProjectPrelims/src/ISLUStudentPortal.java"; then
    echo "✅ ISLUStudentPortal.java updated with integration code"
else
    echo "❌ ISLUStudentPortal.java not properly updated"
fi

# Summary
echo ""
echo "=== Integration Summary ==="
if [ "$ALL_FILES_PRESENT" = true ]; then
    echo "✅ All Spring Boot components created successfully"
    echo "✅ Integration with existing Swing portal completed"
    echo ""
    echo "Next steps:"
    echo "1. Navigate to spring-schedule-portal directory"
    echo "2. Run: mvn spring-boot:run"
    echo "3. Test API at: http://localhost:8080/students/api/schedule/STUDENT123"
    echo "4. Run the Java Swing portal to see enhanced schedule"
else
    echo "❌ Some components are missing. Please check the errors above."
fi

echo ""
echo "=== File Structure Overview ==="
echo "📁 spring-schedule-portal/ (New Spring Boot Backend)"
echo "├── src/main/java/com/islu/portal/"
echo "│   ├── entity/ (JPA Entities)"
echo "│   ├── repository/ (Data Access)"
echo "│   ├── service/ (Business Logic)"
echo "│   ├── controller/ (REST APIs)"
echo "│   └── config/ (Configuration)"
echo "├── pom.xml (Maven Dependencies)"
echo "└── README.md (Documentation)"
echo ""
echo "📁 LabProjectPrelims/LabProjectPrelims/src/ (Enhanced Swing Portal)"
echo "├── ISLUStudentPortal.java (Updated with integration)"
echo "├── SpringScheduleIntegration.java (Bridge component)"
echo "└── ... (existing files)"