package com.skillsphere.entity;

import jakarta.persistence.*;

@Entity
@Table(name="student")
public class Student {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(nullable=false)
    private String name;
    private String contact;
    @Column(length=1000)
    private String bio;

    public Long getId(){return id;}
    public Long getUserId(){return userId;}
    public String getName(){return name;}
    public String getContact(){return contact;}
    public String getBio(){return bio;}
    public void setId(Long id){this.id=id;}
    public void setUserId(Long userId){this.userId=userId;}
    public void setName(String name){this.name=name;}
    public void setContact(String contact){this.contact=contact;}
    public void setBio(String bio){this.bio=bio;}
}
