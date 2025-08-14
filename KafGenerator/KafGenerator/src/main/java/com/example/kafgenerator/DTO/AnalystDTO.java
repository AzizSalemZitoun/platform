package com.example.kafgenerator.DTO;

import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.Roles;
import java.util.ArrayList;
import java.util.List;

public class AnalystDTO {
Long id;
String userName;
String password;
Roles role;
    private List<Project> projects=new ArrayList<>();


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Project> getProjects() {
return projects;
}

public void setProjects(List<Project> projects) {
this.projects = projects;
}

public Long getId() {
return id;
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

public Roles getRole() {
return role;
}

public void setRole(Roles role) {
this.role = role;
}
}
