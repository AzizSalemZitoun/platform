package com.example.kafgenerator.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Project {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
    @NotBlank
private String name;
    @Enumerated(EnumType.STRING)
    @NotBlank
    private ClientType clientType;
    @Enumerated(EnumType.STRING)
    private IndividualType individualType;
    @NotBlank
    private String description;
private String createdAt;
@ManyToOne()
private User user;
@OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
@JsonBackReference
private List<Document> documents=new ArrayList<>();

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

public List<Document> getDocuments() {
return documents;
}

public void setDocuments(List<Document> documents) {
this.documents = documents;
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

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}
    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public IndividualType getIndividualType() {
        return individualType;
    }

    public void setIndividualType(IndividualType individualType) {
        this.individualType = individualType;
    }
}
