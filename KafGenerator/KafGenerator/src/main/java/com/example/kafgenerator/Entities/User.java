package com.example.kafgenerator.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
@Entity
public class User {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
@NotBlank
private String password;
@NotBlank
private String userName;
@ManyToOne(cascade = CascadeType.ALL)
private Roles role;
@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
@JsonBackReference
private List<Project> projects= new ArrayList<>();

public List<Project> getProjects() {
return projects;
}

public void setProjects(List<Project> projects) {
this.projects = projects;
}

public Long getId() {
return id;
}

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public void setId(Long id) {
this.id = id;
}

public String getPassword() {
return password;
}

public void setPassword(String password) {
this.password = password;
}

public String getUserName() {
        return userName;
    }

public void setUserName(String userName) {
        this.userName = userName;
    }
}
