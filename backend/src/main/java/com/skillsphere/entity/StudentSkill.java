package com.skillsphere.entity;

import jakarta.persistence.*;

@Entity
@Table(name="student_skill")
public class StudentSkill {
    @EmbeddedId
    private StudentSkillId id;
    private String level;

    public StudentSkillId getId(){return id;}
    public String getLevel(){return level;}
    public void setId(StudentSkillId id){this.id=id;}
    public void setLevel(String level){this.level=level;}
}
