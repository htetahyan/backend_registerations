package com.registeration.backend.service;


import com.amazonaws.services.s3.AmazonS3;
import com.registeration.backend.entity.Documents;
import com.registeration.backend.entity.ExistingTeacher;
import com.registeration.backend.repository.ExistingTeacherRepo;
import com.registeration.backend.service.ExistingTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExistingTeacherServiceImpl implements ExistingTeacherService {
    @Value("${bucketName}")
    private String bucketName;
    @Autowired
    private final AmazonS3 s3;
    @Autowired
    private ExistingTeacherRepo existingTeacherRepo;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        try {
            File file1 = convertMultipartToFile(multipartFile);
            s3.putObject(bucketName, fileName, file1);
          return s3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File converted=new File(file.getOriginalFilename());
        FileOutputStream fos=new FileOutputStream(converted);
        fos.write(file.getBytes());
        fos.close();
        return converted;
    }



    @Override
    public ExistingTeacher addTeacher(ExistingTeacher existingTeacher) {
        return existingTeacherRepo.save(existingTeacher);
    }

    @Override
    public List<ExistingTeacher> getAllTeacher() {
        return existingTeacherRepo.findAll();
    }

    @Override
    public ExistingTeacher getTeacherById(Long teacherId) {
        return existingTeacherRepo.findById(teacherId)
            .orElse(null); // You might want to throw an exception if not found, depending on your use case
    }
    @Override

    public ResponseEntity<String> deleteTeacherById(Long id) {
        try {
            existingTeacherRepo.deleteById(id);
            return ResponseEntity.ok("Teacher with ID " + id + " deleted successfully");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher with ID " + id + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the teacher");
        }
    }

    @Override
    public boolean isTeacherIdUnique(String teacherId) {
        return false;
    }

    /*@Override
    public Teacher isTeacherIdUnique(String teacherId) {
        return teacherRepo.findByTeacherId(teacherId) ;    }*/

    @Override
    public ExistingTeacher getTeacherByTeacherId(String teacherId) {
        return existingTeacherRepo.findByTeacherId(teacherId);
    }

    @Override
    public ExistingTeacher updateTeacher(ExistingTeacher existingTeacher) {
        return existingTeacherRepo.save(existingTeacher);
    }

    @Override
    public List<ExistingTeacher> searchTeachers(String searchCriteria) {
        List<ExistingTeacher> matchingExistingTeachers = new ArrayList<>();
        List<ExistingTeacher> existingTeachers =existingTeacherRepo.findAll();
        for (ExistingTeacher existingTeacher : existingTeachers) {
            if (existingTeacher.getTeacherId().contains(searchCriteria)) {
                matchingExistingTeachers.add(existingTeacher);
            }
        }

        return matchingExistingTeachers;    }
}



   /* @Override
    public List<Teacher> getTeacherById(Long id) {
        return teacherRepo.findById(id);
    }*/



