package com.example.kafgenerator.Services;

import com.example.kafgenerator.DTO.ProjectDTO;
import com.example.kafgenerator.Entities.Project;

import java.io.IOException;
import java.util.List;

public interface IProjectService {
    ProjectDTO addProject(ProjectDTO projectDTO);

    void deleteProject(Long id);

    Project getProject(Long id);

    Project updateProject(Long id, Project updatedProject);

    List<ProjectDTO> getAllProjects();

    Project setProjectToUser(Long id, Long idp);

    //String generateReport(Project project);

     //String generateReport(Long id, Long idprod);

String generateReport(Long id, Long idprod) throws IOException, InterruptedException;

    //String generateReport(String prompt);
}
