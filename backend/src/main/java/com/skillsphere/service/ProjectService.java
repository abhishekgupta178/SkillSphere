package com.skillsphere.service;

import com.skillsphere.entity.Project;
import com.skillsphere.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository repo;
    public ProjectService(ProjectRepository repo){this.repo=repo;}
    public List<Project> all(){return repo.findAll();}
    public Project get(Long id){return repo.findById(id).orElseThrow();}
    public Project save(Project item){return repo.save(item);}
    public void delete(Long id){repo.deleteById(id);}
}
