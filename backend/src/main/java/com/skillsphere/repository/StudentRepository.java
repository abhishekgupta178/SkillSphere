package com.skillsphere.repository;

import com.skillsphere.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByUserId(Long userId);

    @Query("select distinct s from Student s, StudentSkill ss, Skill sk " +
           "where ss.id.studentId = s.id and ss.id.skillId = sk.id " +
           "and lower(sk.name) like lower(concat('%', :skill, '%'))")
    List<Student> searchBySkill(@Param("skill") String skill);
}
