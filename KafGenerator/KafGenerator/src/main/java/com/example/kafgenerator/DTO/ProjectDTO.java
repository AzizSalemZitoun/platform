package com.example.kafgenerator.DTO;

import com.example.kafgenerator.Entities.ClientType;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.IndividualType;
import com.example.kafgenerator.Entities.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {

    private Long id;
    private String name;
    private String description;
    private String createdAt;
    public User user;
    private ClientType clientType;
    private IndividualType individualType;
    private List<com.example.kafgenerator.Entities.Document> documents= new ArrayList<>();

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

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
