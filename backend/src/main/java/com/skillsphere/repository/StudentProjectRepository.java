package com.skillsphere.repository;

import com.skillsphere.entity.StudentProject;
import com.skillsphere.entity.StudentProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentProjectRepository extends JpaRepository<StudentProject, StudentProjectId> {
    List<StudentProject> findByIdStudentId(Long studentId);
}
