package com.skillsphere.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentProjectId implements Serializable {
    private Long studentId;
    private Long projectId;

    public StudentProjectId(){}
    public StudentProjectId(Long studentId, Long projectId){this.studentId=studentId;this.projectId=projectId;}
    public Long getStudentId(){return studentId;}
    public Long getProjectId(){return projectId;}
    public void setStudentId(Long v){studentId=v;}
    public void setProjectId(Long v){projectId=v;}

    @Override public boolean equals(Object o){
        if(this==o)return true;
        if(!(o instanceof StudentProjectId x))return false;
        return Objects.equals(studentId,x.studentId)&&Objects.equals(projectId,x.projectId);
    }
    @Override public int hashCode(){return Objects.hash(studentId,projectId);}
}
