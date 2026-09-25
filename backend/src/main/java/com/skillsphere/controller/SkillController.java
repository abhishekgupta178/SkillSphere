package com.skillsphere.controller;

import com.skillsphere.entity.Skill;
import com.skillsphere.service.SkillService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins="*")
public class SkillController {
    private final SkillService service;
    public SkillController(SkillService service){this.service=service;}

    @GetMapping
    public List<Skill> all(){return service.all();}

    @GetMapping("/{id}")
    public Skill get(@PathVariable Long id){return service.get(id);}

    @PostMapping
    public Skill create(@RequestBody Skill item){return service.save(item);}

    @PutMapping("/{id}")
    public Skill update(@PathVariable Long id,@RequestBody Skill item){
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){service.delete(id);}
}
