package com.skillsphere.controller;

import com.skillsphere.entity.Project;
import com.skillsphere.entity.Student;
import com.skillsphere.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins="*")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service){this.service=service;}

    public record SkillAssignment(Long skillId, String level) {}

    @GetMapping
    public List<Student> all(){return service.all();}

    @GetMapping("/search")
    public List<Map<String,Object>> search(@RequestParam String skill){return service.searchBySkill(skill);}

    @GetMapping("/{id}")
    public Student get(@PathVariable Long id){return service.get(id);}

    @PostMapping
    public Student create(@RequestBody Student item){return service.save(item);}

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id,@RequestBody Student item){
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){service.delete(id);}

    @GetMapping("/{id}/skills")
    public List<Map<String,Object>> skills(@PathVariable Long id){return service.skills(id);}

    @PostMapping("/{id}/skills")
    public Map<String,Object> addSkill(@PathVariable Long id,@RequestBody SkillAssignment body){
        if(body.skillId()==null) throw new IllegalArgumentException("skillId is required");
        return service.addSkill(id, body.skillId(), body.level());
    }

    @DeleteMapping("/{id}/skills/{skillId}")
    public void removeSkill(@PathVariable Long id,@PathVariable Long skillId){service.removeSkill(id, skillId);}

    @GetMapping("/{id}/projects")
    public List<Project> projects(@PathVariable Long id){return service.projects(id);}

    @PostMapping("/{id}/projects/{projectId}")
    public Project addProject(@PathVariable Long id,@PathVariable Long projectId){return service.addProject(id, projectId);}

    @DeleteMapping("/{id}/projects/{projectId}")
    public void removeProject(@PathVariable Long id,@PathVariable Long projectId){service.removeProject(id, projectId);}
}
