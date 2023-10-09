package com.registeration.backend.repository;

import com.registeration.backend.entity.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest


class TeacherRepoTest {
    @Autowired
private TeacherRepo teacherRepo;
    @Test
public void saveTeacher(){
    Teacher teacher= Teacher.builder().address("s").DOB(123434343L).gender("male").idImg("sds").address("sdsd").fullName("john").experience(1).languagesPro("english").subjectTaught("myanmal").build();
teacherRepo.save(teacher);
}
}
