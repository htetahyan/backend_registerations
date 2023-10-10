package com.registeration.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String studentName;
    private String email;
    private String studentDob;
    private String fatherName;
    private String passedGrade;
    private String gradeToAttend;
    private String campusTime;
    private String intakes;
    private String oldSchoolName;
    private String docUrl;
    private String city;
    private String country;
    private String phoneNumber;
    private String address;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
