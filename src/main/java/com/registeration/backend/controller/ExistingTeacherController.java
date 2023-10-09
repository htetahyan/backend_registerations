package com.registeration.backend.controller;

import com.registeration.backend.entity.ExistingTeacher;
import com.registeration.backend.service.ExistingTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExistingTeacherController {

    private final ExistingTeacherService existingTeacherService;


    @PostMapping(value = {"/add-teacher"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addTeacher(@RequestPart("teacher") ExistingTeacher existingTeacher, @RequestPart("imageFile") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Image file is empty.");
            }

            String imageUrl = uploadFile(file);
            if (imageUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the image file.");
            }

            // Check if the teacherId is unique before creating the teacher
            if (existingTeacherService.getTeacherByTeacherId(existingTeacher.getTeacherId())!=null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TeacherId already exists.");
            }

            existingTeacher.setImageUrl(imageUrl);
            ExistingTeacher addedExistingTeacher = existingTeacherService.addTeacher(existingTeacher);

            if (addedExistingTeacher == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add teacher.");
            }

            return ResponseEntity.ok(addedExistingTeacher);
        } catch (Exception e) {
            // Handle exceptions gracefully and provide an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @PatchMapping(value = {"/update-teacher/{id}"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateTeacher(
        @PathVariable Long id,
        @RequestPart(name = "teacher", required = false) ExistingTeacher updatedExistingTeacher,
        @RequestPart(name = "imageFile", required = false) MultipartFile file) {
        try {
            // Retrieve the existing teacher from the database
            ExistingTeacher existingTeacher = existingTeacherService.getTeacherById(id);

            if (existingTeacher == null) {
                return ResponseEntity.notFound().build();
            }
            if (areTeachersEqual(existingTeacher, updatedExistingTeacher,file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No changes were made.");
            }
            // Update teacher's fields if provided in the request
            if (updatedExistingTeacher != null) {
                if (!existingTeacher.getTeacherId().equals(updatedExistingTeacher.getTeacherId()) &&
                    existingTeacherService.getTeacherByTeacherId(updatedExistingTeacher.getTeacherId()) != null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TeacherId already exists in another teacher.");
                }
                // Apply updates to the existing teacher object
                if (updatedExistingTeacher.getName() != null) {
                    existingTeacher.setName(updatedExistingTeacher.getName());
                }

                if (updatedExistingTeacher.getPosition() != null) {
                    existingTeacher.setPosition(updatedExistingTeacher.getPosition());
                }

                if (updatedExistingTeacher.getTeacherId() != null) {
                    existingTeacher.setTeacherId(updatedExistingTeacher.getTeacherId());
                }
                if (updatedExistingTeacher.getJoinDate() != null) {
                    existingTeacher.setJoinDate(updatedExistingTeacher.getJoinDate());
                }
                if (updatedExistingTeacher.getDepartment() != null) {
                    existingTeacher.setDepartment(updatedExistingTeacher.getDepartment());
                }
                if (updatedExistingTeacher.getEmergencyContactPerson() != null) {
                    existingTeacher.setEmergencyContactPerson(updatedExistingTeacher.getEmergencyContactPerson());
                }
                if (updatedExistingTeacher.getNrcNo() != null) {
                    existingTeacher.setNrcNo(updatedExistingTeacher.getNrcNo());
                }
                if (updatedExistingTeacher.getPhoneNo() != null) {
                    existingTeacher.setPhoneNo(updatedExistingTeacher.getPhoneNo());
                }
                if (updatedExistingTeacher.getEmergencyContactNo() != null) {
                    existingTeacher.setEmergencyContactNo(updatedExistingTeacher.getEmergencyContactNo());
                }

                // ... add other fields as needed
            }

            // Handle image file update if provided
            if (file != null && !file.isEmpty()) {
                String imageUrl = uploadFile(file);
                if (imageUrl == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the image file.");
                }
                existingTeacher.setImageUrl(imageUrl);
            }

            // Save the updated teacher back to the database
            ExistingTeacher updated = existingTeacherService.updateTeacher(existingTeacher);

            if (updated == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update teacher.");
            }

            return ResponseEntity.ok(existingTeacher);
        } catch (Exception e) {
            // Handle exceptions gracefully and provide an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    private boolean areTeachersEqual(ExistingTeacher existingTeacher1, ExistingTeacher existingTeacher2, MultipartFile file) {
        if (existingTeacher1 == null || existingTeacher2 == null) {
            return false; // Handle null objects as needed
        }

        boolean areImageUrlEqual = file == null || file.isEmpty();
        // Compare all relevant fields here to check if they are equal
        boolean areNamesEqual = Objects.equals(existingTeacher1.getName(), existingTeacher2.getName());
        boolean arePositionsEqual = Objects.equals(existingTeacher1.getPosition(), existingTeacher2.getPosition());

        boolean areTeacherIdsEqual = Objects.equals(existingTeacher1.getTeacherId(), existingTeacher2.getTeacherId());
        boolean areJoinDatesEqual = Objects.equals(existingTeacher1.getJoinDate(), existingTeacher2.getJoinDate());
        boolean areDepartmentsEqual = Objects.equals(existingTeacher1.getDepartment(), existingTeacher2.getDepartment());
        boolean areEmergencyContactPersonsEqual = Objects.equals(existingTeacher1.getEmergencyContactPerson(), existingTeacher2.getEmergencyContactPerson());
        boolean areNrcNosEqual = Objects.equals(existingTeacher1.getNrcNo(), existingTeacher2.getNrcNo());
        boolean arePhoneNosEqual = Objects.equals(existingTeacher1.getPhoneNo(), existingTeacher2.getPhoneNo());
        boolean areEmergencyContactNosEqual = Objects.equals(existingTeacher1.getEmergencyContactNo(), existingTeacher2.getEmergencyContactNo());

        // Add other comparisons for additional fields as needed
        // For example, if you have more fields like email, address, etc., add them here

        // Return true if all fields are equal, otherwise return false
        return areNamesEqual && arePositionsEqual && areTeacherIdsEqual &&
            areJoinDatesEqual && areDepartmentsEqual &&
            areEmergencyContactPersonsEqual && areNrcNosEqual &&
            arePhoneNosEqual && areEmergencyContactNosEqual && areImageUrlEqual;
    }

    @DeleteMapping("/delete-teachers/{id}")
    @CrossOrigin("*")
    public ResponseEntity<String> deleteTeacher(@PathVariable("id") Long id){
        return existingTeacherService.deleteTeacherById(id);
    }
    @GetMapping("teachers/teacherId/{teacherId}")
    public String getTeacherIdByTeacherId(@PathVariable("teacherId") String teacherId) {
        ExistingTeacher existingTeacher = existingTeacherService.getTeacherByTeacherId(teacherId);

        if (existingTeacher != null) {
            return existingTeacher.getTeacherId(); // Assuming there's a method to get the teacherId from the Teacher object
        } else {
            // Handle the case where no teacher with the specified ID was found
            return "Teacher not found";
        }
    }


    @RequestMapping ("/")
    public String home(){
        return "home";
    }


    @GetMapping("/teachers")
    public ResponseEntity<List<ExistingTeacher>> getAllTeacher() {
        try {
            List<ExistingTeacher> existingTeachers = (List<ExistingTeacher>) existingTeacherService.getAllTeacher();
            return new ResponseEntity<>(existingTeachers, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*@PutMapping("/update-teacher/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Long id, @RequestPart("teacher") Teacher teacher, @RequestPart("imageFile") MultipartFile file) {
        try {
            Teacher existingTeacher = teacherSevice.getTeacherById(id);

            if (existingTeacher == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update the existing teacher's information
            existingTeacher.setName(teacher.getName());


            // Handle image upload
            if (file != null) {
                String imageUrl = uploadFile(file);
                existingTeacher.setImageUrl(imageUrl);
            }
            // Save the updated teacher
            Teacher updatedTeacher = teacherSevice.updateTeacher(existingTeacher);
            return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/

    @GetMapping("teachers/search")
    public ResponseEntity<List<ExistingTeacher>> searchTeachers(@RequestParam("criteria") String searchCriteria) {
        List<ExistingTeacher> matchingExistingTeachers = existingTeacherService.searchTeachers(searchCriteria);

        if (!matchingExistingTeachers.isEmpty()) {
            return ResponseEntity.ok(matchingExistingTeachers);
        } else {
            // Handle the case where no matching teachers were found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("teachers/{id}")
    public ResponseEntity<?> getTeacherById( @PathVariable(value = "id",required = false) Long id) {
        ExistingTeacher existingTeacher = existingTeacherService.getTeacherById(id);

        if (existingTeacher == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(existingTeacher);
    }
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String imageURL = existingTeacherService.uploadFile(multipartFile);

        return imageURL;
    }
}
