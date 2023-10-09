package com.registeration.backend.service;

import com.registeration.backend.entity.ExistingTeacher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExistingTeacherService {
    String uploadFile(MultipartFile multipartFile) throws IOException;

    ExistingTeacher addTeacher(ExistingTeacher existingTeacher);


    List<ExistingTeacher> getAllTeacher();

    ExistingTeacher getTeacherById(Long id);

    ResponseEntity<String> deleteTeacherById(Long id);

    boolean isTeacherIdUnique(String teacherId);
    ExistingTeacher getTeacherByTeacherId(String teacherId);

    ExistingTeacher updateTeacher(ExistingTeacher existingTeacher);

    List<ExistingTeacher> searchTeachers(String searchCriteria);



    /*Teacher updateTeacher(Teacher existingTeacher);*/
}
