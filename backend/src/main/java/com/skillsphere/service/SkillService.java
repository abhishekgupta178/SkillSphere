package com.skillsphere.service;

import com.skillsphere.entity.Skill;
import com.skillsphere.repository.SkillRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillService {
    private final SkillRepository repo;
    public SkillService(SkillRepository repo){this.repo=repo;}
    public List<Skill> all(){return repo.findAll();}
    public Skill get(Long id){return repo.findById(id).orElseThrow();}
    public Skill save(Skill item){return repo.save(item);}
    public void delete(Long id){repo.deleteById(id);}
}
