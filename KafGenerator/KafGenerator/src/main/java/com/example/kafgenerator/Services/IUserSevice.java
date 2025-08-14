package com.example.kafgenerator.Services;
import com.example.kafgenerator.DTO.AnalystDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import java.util.List;

public interface IUserSevice {

    AnalystDTO registerUser(AnalystDTO analystDTO);

    // Get all users
    List<User> getAllUsers();

    // Update user
    User updateUser(Long id, User updatedUser);

    // Delete user
    void deleteUser(Long id);
   //fetch user by id
   User getUser(Long id);

    Boolean exists(AnalystDTO analystDTO);

    List<Document> getDocumentsByUser(Long id);

    List<Project> getProjectsByUser(Long id);

    String authenticate(User user);
}
