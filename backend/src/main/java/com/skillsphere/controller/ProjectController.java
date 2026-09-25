package com.skillsphere.controller;

import com.skillsphere.entity.Project;
import com.skillsphere.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins="*")
public class ProjectController {
    private final ProjectService service;
    public ProjectController(ProjectService service){this.service=service;}

    @GetMapping
    public List<Project> all(){return service.all();}

    @GetMapping("/{id}")
    public Project get(@PathVariable Long id){return service.get(id);}

    @PostMapping
    public Project create(@RequestBody Project item){return service.save(item);}

    @PutMapping("/{id}")
    public Project update(@PathVariable Long id,@RequestBody Project item){
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){service.delete(id);}
}
