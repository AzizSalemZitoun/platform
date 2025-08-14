package com.example.kafgenerator.Entities;

import jakarta.persistence.*;


@Entity
public class Document {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String name;
@Enumerated(EnumType.STRING)
private Type type;
@Lob
@Basic(fetch = FetchType.LAZY)
private byte[] content;
@ManyToOne()
private Project project;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public Project getProject() {
return project;
}

public void setProject(Project project) {
this.project = project;
}
}
