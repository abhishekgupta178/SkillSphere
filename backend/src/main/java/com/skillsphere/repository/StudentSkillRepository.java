package com.skillsphere.repository;

import com.skillsphere.entity.StudentSkill;
import com.skillsphere.entity.StudentSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentSkillRepository extends JpaRepository<StudentSkill, StudentSkillId> {
    List<StudentSkill> findByIdStudentId(Long studentId);
}
