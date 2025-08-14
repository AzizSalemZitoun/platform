
package com.example.kafgenerator.Services;


import com.example.kafgenerator.DTO.AnalystDTO;
import com.example.kafgenerator.DTO.DocumentDTO;
import com.example.kafgenerator.DTO.ProjectDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    //User
    AnalystDTO userToAnalyst(User user);
    User analystToUser(AnalystDTO analystDTO);
    List<User> analystListToUser(List<AnalystDTO> analystDTOList);
    List<AnalystDTO> UserListToAnalyst(List<User> userList);

    //Project
    Project dtoToProject(ProjectDTO projectDTO);
    ProjectDTO projectToDTO(Project project);
    List<ProjectDTO> projectListToDTO(List<Project> projectList);
    List<Project> dTOListToProject(List<ProjectDTO> projectDTOList);

    //DOCUMENT
    Document dtoToDocument (DocumentDTO documentDTO);
    DocumentDTO documentToDTO(Document document);
    List<DocumentDTO> documentListToDTO(List<Document> documentList);
    List<Document>documentoDocument(List<Document> documentList);


}