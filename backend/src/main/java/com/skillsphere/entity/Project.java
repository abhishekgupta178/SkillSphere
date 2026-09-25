package com.skillsphere.entity;

import jakarta.persistence.*;

@Entity
@Table(name="project")
public class Project {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String title;
    @Column(length=2000)
    private String description;
    @Column(name="github_url", length=500)
    private String githubUrl;

    public Long getId(){return id;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public String getGithubUrl(){return githubUrl;}
    public void setId(Long id){this.id=id;}
    public void setTitle(String title){this.title=title;}
    public void setDescription(String description){this.description=description;}
    public void setGithubUrl(String githubUrl){this.githubUrl=githubUrl;}
}
