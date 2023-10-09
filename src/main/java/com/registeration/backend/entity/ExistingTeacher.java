package com.registeration.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExistingTeacher {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;
    private  String position;
    @Column(unique = true, nullable = false)
    private String teacherId;
    private Long JoinDate;
    private String department;
    private String emergencyContactPerson;
    private String NrcNo;
    private Long phoneNo;
    private String emergencyContactNo;
    private String ImageUrl;

}
