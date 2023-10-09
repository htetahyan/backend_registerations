package com.registeration.backend.repository;

import com.registeration.backend.entity.ExistingTeacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExistingTeacherRepo extends JpaRepository<ExistingTeacher,Long> {
    public ExistingTeacher findByTeacherId(String teacherId);
}
