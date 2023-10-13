package com.registeration.backend.service;

import com.registeration.backend.entity.Student;

import java.util.List;

public interface StudentService {
    Student saveStudent(Student student);

    List<Student> getAllStudent();

    String deleteStudent(Long id);

    Student getStudentById(Long id);


}
