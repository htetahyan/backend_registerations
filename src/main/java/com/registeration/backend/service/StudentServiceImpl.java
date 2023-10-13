package com.registeration.backend.service;

import com.registeration.backend.entity.Student;
import com.registeration.backend.exceptions.NotFoundException;
import com.registeration.backend.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepo studentRepo;
    @Override
    public Student saveStudent(Student student) {
      return studentRepo.save(student);


    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }

    @Override
    public String deleteStudent(Long id) {
         studentRepo.deleteById(id);
         return "deleted student with id"+ id;
    }

    @Override
    public Student getStudentById(Long id) {
        Optional<Student> optionalStudent = studentRepo.findById(id);

        if (optionalStudent.isPresent()) {
            return optionalStudent.get(); // Student found, return it
        } else {
            throw new NotFoundException("Student not found with ID: " + id); // Student not found, throw a custom exception
        }
    }

}
