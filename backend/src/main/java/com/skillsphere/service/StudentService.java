package com.skillsphere.service;

import com.skillsphere.entity.*;
import com.skillsphere.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepo;
    private final SkillRepository skillRepo;
    private final ProjectRepository projectRepo;
    private final StudentSkillRepository studentSkillRepo;
    private final StudentProjectRepository studentProjectRepo;

    public StudentService(StudentRepository studentRepo, SkillRepository skillRepo,
                          ProjectRepository projectRepo, StudentSkillRepository studentSkillRepo,
                          StudentProjectRepository studentProjectRepo){
        this.studentRepo=studentRepo;
        this.skillRepo=skillRepo;
        this.projectRepo=projectRepo;
        this.studentSkillRepo=studentSkillRepo;
        this.studentProjectRepo=studentProjectRepo;
    }

    public List<Student> all(){return studentRepo.findAll();}
    public Student get(Long id){return studentRepo.findById(id).orElseThrow();}
    public Student save(Student item){return studentRepo.save(item);}
    public void delete(Long id){studentRepo.deleteById(id);}

    public List<Map<String,Object>> searchBySkill(String skillName){
        if(skillName == null || skillName.isBlank()) return List.of();
        return studentRepo.searchBySkill(skillName.trim()).stream().map(this::studentResult).toList();
    }

    private Map<String,Object> studentResult(Student student){
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("id", student.getId());
        result.put("name", student.getName());
        result.put("contact", student.getContact());
        result.put("bio", student.getBio());
        result.put("skills", skills(student.getId()).stream().map(x -> x.get("name")).toList());
        return result;
    }

    public List<Map<String,Object>> skills(Long studentId){
        List<Map<String,Object>> result = new ArrayList<>();
        for(StudentSkill link : studentSkillRepo.findByIdStudentId(studentId)){
            Skill skill = skillRepo.findById(link.getId().getSkillId()).orElse(null);
            if(skill == null) continue;
            Map<String,Object> item = new LinkedHashMap<>();
            item.put("id", skill.getId());
            item.put("name", skill.getName());
            item.put("category", skill.getCategory());
            item.put("level", link.getLevel());
            result.add(item);
        }
        return result;
    }

    public Map<String,Object> addSkill(Long studentId, Long skillId, String level){
        if(!studentRepo.existsById(studentId)) throw new IllegalArgumentException("Student not found");
        if(!skillRepo.existsById(skillId)) throw new IllegalArgumentException("Skill not found");
        StudentSkill link = new StudentSkill();
        link.setId(new StudentSkillId(studentId, skillId));
        link.setLevel(level == null || level.isBlank() ? "Intermediate" : level);
        studentSkillRepo.save(link);
        return skills(studentId).stream().filter(x -> Objects.equals(x.get("id"), skillId)).findFirst().orElseThrow();
    }

    public void removeSkill(Long studentId, Long skillId){
        studentSkillRepo.deleteById(new StudentSkillId(studentId, skillId));
    }

    public List<Project> projects(Long studentId){
        List<Project> result = new ArrayList<>();
        for(StudentProject link : studentProjectRepo.findByIdStudentId(studentId)){
            projectRepo.findById(link.getId().getProjectId()).ifPresent(result::add);
        }
        return result;
    }

    public Project addProject(Long studentId, Long projectId){
        if(!studentRepo.existsById(studentId)) throw new IllegalArgumentException("Student not found");
        if(!projectRepo.existsById(projectId)) throw new IllegalArgumentException("Project not found");
        StudentProject link = new StudentProject();
        link.setId(new StudentProjectId(studentId, projectId));
        studentProjectRepo.save(link);
        return projectRepo.findById(projectId).orElseThrow();
    }

    public void removeProject(Long studentId, Long projectId){
        studentProjectRepo.deleteById(new StudentProjectId(studentId, projectId));
    }
}
