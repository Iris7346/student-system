package com.example.studentsystem.entity;

import lombok.Data;

@Data
public class Course {
    private Long id;
    private String name;
    private Long teacherId;
    private Integer maxStudents;
    private String description;
}