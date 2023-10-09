package com.registeration.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIgnoreProperties
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String gender;
    private String age;
    private String DOB;
    private String address;
    private String phoneNumber;
    private String subjectTaught;
    private String experience;
    private String languagesPro;

    @Column(columnDefinition = "varchar(255) default 'pending'")
    private String verified;
    @JsonManagedReference

    @OneToMany(mappedBy = "teacher")
    private List<Documents> documents;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
