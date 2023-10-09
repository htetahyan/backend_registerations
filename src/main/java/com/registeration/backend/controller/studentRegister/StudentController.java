package com.registeration.backend.controller.studentRegister;

import com.registeration.backend.entity.Student;
import com.registeration.backend.service.ExistingTeacherService;
import com.registeration.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Controller
@RestController
public class StudentController {

    private final ExistingTeacherService existingTeacherService;
    @Autowired

    private StudentService studentService;

    public StudentController(ExistingTeacherService existingTeacherService) {
        this.existingTeacherService = existingTeacherService;
    }
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
    }}

