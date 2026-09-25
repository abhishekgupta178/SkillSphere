package com.skillsphere.repository;
import com.skillsphere.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProjectRepository extends JpaRepository<Project,Long> {}
