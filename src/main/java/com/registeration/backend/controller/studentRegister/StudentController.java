package com.registeration.backend.controller.studentRegister;

import com.registeration.backend.entity.ExistingTeacher;
import com.registeration.backend.entity.Student;
import com.registeration.backend.entity.Teacher;
import com.registeration.backend.exceptions.NotFoundException;
import com.registeration.backend.service.ExistingTeacherService;
import com.registeration.backend.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@CrossOrigin(origins = "*")
public class StudentController {
@Autowired
    private  ExistingTeacherService existingTeacherService;
    @Autowired
    private StudentService studentService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @PostMapping(value = "/student/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerStudent(
        @RequestPart("student_data") Student student,
        @RequestPart("student_doc") MultipartFile doc
    ) {
        try {
            if (doc == null || doc.isEmpty()) {
                return ResponseEntity.badRequest().body("Student document file is missing or empty.");
            }

            String doc_url = existingTeacherService.uploadFile(doc);
            if (doc_url == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload student document.");
            }

            student.setDocUrl(doc_url);
            Student response = studentService.saveStudent(student);
            if (response!=null) {
                return ResponseEntity.ok("Student registered successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register student.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during file upload.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred."+e.getMessage());
        }
    }


    @GetMapping("/student/registered")
    public ResponseEntity<?> getAllStudent() {
        try {
            List<Student> students = studentService.getAllStudent();
            return ResponseEntity.ok(students);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Students not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
@DeleteMapping("/student/delete/{id}")
public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
    try {
        logger.info("Received request to delete teacher with ID: {}", id);

     Student deletedStudent= studentService.getStudentById(id);

        if (deletedStudent!=null) {
            // Rest of your code
            return ResponseEntity.ok(studentService.deleteStudent(id));
        } else {
            logger.warn("student not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
        }
    } catch (Exception e) {
        logger.error("An unexpected error occurred: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e);
    }


}
    @GetMapping("/student/{id}")

    public ResponseEntity<?> getStudentById( @PathVariable(value = "id",required = false) Long id) {
        Student student = studentService.getStudentById(id);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }
    @PostMapping("/student/update/{id}")

    public ResponseEntity<?> toggleVerified(
        @PathVariable Long id
    ) throws IOException {
        try {
      Student student=studentService.getStudentById(id);

if(student.getVerified()==null){  student.setVerified("success");}else{
    student.setVerified(null);
}


               Student updatedTeacher = studentService.saveStudent(student);
               return ResponseEntity.ok(updatedTeacher.getVerified());

        } catch (Exception e) {
            // Handle exceptions gracefully and provide an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}

