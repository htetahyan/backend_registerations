package com.registeration.backend.controller;

import com.registeration.backend.entity.Documents;
import com.registeration.backend.entity.ExistingTeacher;
import com.registeration.backend.entity.Teacher;
import com.registeration.backend.exceptions.NotFoundException;
import com.registeration.backend.service.FileService;
import com.registeration.backend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController

public class TeacherController {
    @Autowired
 private FileService service;
    @Autowired
    private TeacherService teacherService;

    @PostMapping(value = "/register", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> addTeacher(
        @RequestPart(value = "teacher",required = true) Teacher teacher,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @RequestPart(value = "idCard", required = false) MultipartFile idCard,
        @RequestPart(value = "cv", required = false) MultipartFile cv) {
        try {
            // Check if idCard and cv are missing or empty
            if (idCard == null || cv == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("idCard and cv are required.");
            }

            List<MultipartFile> multipartFiles = new ArrayList<>();
            if (files != null) {
                multipartFiles.addAll(files);
            }
            multipartFiles.add(idCard);
            multipartFiles.add(cv);

            Teacher savedTeacher = teacherService.addTeacher(teacher);

            if (savedTeacher == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add teacher. Please try again.");
            }

            List<String> fileUrls = service.saveFiles(multipartFiles, savedTeacher);

            return ResponseEntity.status(HttpStatus.OK).body("Teacher added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add teacher: " + e.getMessage());
        }
    }

    @GetMapping("/registered")
    public ResponseEntity<?> getAllTeacher() {
        try {
            List<Teacher> teachers = teacherService.getAllTeacher();

            if (teachers.isEmpty()) {
                // Return a custom response if no teachers are found
                if (teachers.isEmpty()) {
                    throw new NotFoundException("No teachers found.");
                }
            }

            return new ResponseEntity<>(teachers, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            String errorMessage = "Failed to retrieve teachers: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    @PostMapping(value = {"/registered/approve/{id}"})
    public ResponseEntity<?> approve(
        @PathVariable Long id
  ) {
        try {
       Teacher teacher=getTeacherById(id);
       if(teacher.getVerified()==null){
            teacher.setVerified("success");}
       else{
           teacher.setVerified(null);
       }
            Teacher updatedTeacher=teacherService.addTeacher(teacher);
return  ResponseEntity.ok(updatedTeacher.getVerified());

        } catch (Exception e) {
            // Handle exceptions gracefully and provide an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @GetMapping("registered/{id}")
    public Teacher getTeacherById( @PathVariable(value = "id",required = false) Long id) {
        Teacher existingTeacher = teacherService.getTeacherById(id);

        if (existingTeacher == null) {
            return null;
        }

        return existingTeacher;
    }

    @PostMapping(value = {"/registered/reject/{id}"})
    public ResponseEntity<?> reject(
        @PathVariable Long id
    ) throws IOException {
        try {
            Teacher teacher=getTeacherById(id);
            teacher.setVerified(null);
            Teacher updatedTeacher=teacherService.addTeacher(teacher);
            return  ResponseEntity.ok(updatedTeacher.getVerified());

        } catch (Exception e) {
            // Handle exceptions gracefully and provide an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @DeleteMapping("registered/delete/{id}")
    public ResponseEntity<?> deleteTeacherById(@PathVariable Long id) {
        try {
            Teacher deletedTeacher = teacherService.getTeacherById(id); // Get the teacher to retrieve associated documents
            if (deletedTeacher != null) {
                List<Documents> doc = deletedTeacher.getDocuments();

                // Delete the associated documents
                for (Documents document : doc) {
                    // You need to implement a method to delete documents
                    service.deleteDocumentById(document.getId());
                }

                // Now, delete the teacher
                teacherService.deleteTeacherById(id);

                return ResponseEntity.ok("Teacher and associated documents deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e);
        }
    }


    @GetMapping("/get/{filename}")
 public byte[] download(@PathVariable("filename") String filename){
        return  service.downloadFile(filename);
    }
}
