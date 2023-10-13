package com.registeration.backend.service;

import com.registeration.backend.entity.Teacher;
import com.registeration.backend.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService{
    @Autowired
    private TeacherRepo teacherRepo;
    @Override
    public Teacher addTeacher(Teacher teacher) {
        return teacherRepo.save(teacher);
    }

    @Override
    public List<Teacher> getAllTeacher() {
        return teacherRepo.findAll();
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepo.findById(id)
            .orElse(null);     }

    @Override
    public Teacher deleteTeacherById(Long id) {
         teacherRepo.deleteById(id);
         return null;
    }
}
