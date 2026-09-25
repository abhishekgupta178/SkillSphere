package com.skillsphere.entity;

import jakarta.persistence.*;

@Entity
@Table(name="student_project")
public class StudentProject {
    @EmbeddedId
    private StudentProjectId id;

    public StudentProjectId getId(){return id;}
    public void setId(StudentProjectId id){this.id=id;}
}
