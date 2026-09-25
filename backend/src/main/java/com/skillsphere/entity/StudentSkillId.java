package com.skillsphere.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentSkillId implements Serializable {
    private Long studentId;
    private Long skillId;

    public StudentSkillId(){}
    public StudentSkillId(Long studentId, Long skillId){this.studentId=studentId;this.skillId=skillId;}
    public Long getStudentId(){return studentId;}
    public Long getSkillId(){return skillId;}
    public void setStudentId(Long v){studentId=v;}
    public void setSkillId(Long v){skillId=v;}

    @Override public boolean equals(Object o){
        if(this==o)return true;
        if(!(o instanceof StudentSkillId x))return false;
        return Objects.equals(studentId,x.studentId)&&Objects.equals(skillId,x.skillId);
    }
    @Override public int hashCode(){return Objects.hash(studentId,skillId);}
}
