package com.example.kafgenerator.DTO;

import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class DocumentDTO {

    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String content;
    private Project project;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }


}
