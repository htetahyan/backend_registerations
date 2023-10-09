package com.registeration.backend.service;

import com.registeration.backend.entity.Student;
import com.registeration.backend.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepo studentRepo;
    @Override
    public Student saveStudent(Student student) {
      return studentRepo.save(student);


    }
}
