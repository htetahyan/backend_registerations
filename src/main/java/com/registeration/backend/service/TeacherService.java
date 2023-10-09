package com.registeration.backend.service;

import com.registeration.backend.entity.ExistingTeacher;
import com.registeration.backend.entity.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher addTeacher(Teacher teacher);
    List<Teacher> getAllTeacher();
}
