package com.example.kafgenerator.Services;
import com.example.kafgenerator.DTO.AnalystDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Repositories.RoleRepository;
import com.example.kafgenerator.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements IUserSevice {
    @Autowired
    JwtService jwtService;
@Autowired
AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
   @Autowired
   RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
@Autowired
MyUserDetails userDetailsService;

    @Autowired
    DtoMapper dtoMapper;

    @Override
    public AnalystDTO registerUser(AnalystDTO analystDTO) {
        if(exists(analystDTO)==false){
            User user= dtoMapper.analystToUser(analystDTO);
            user.setRole(roleRepository.findAll().get(0));
             analystDTO.setRole(user.getRole());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return analystDTO;
        }
       return null;
    }

    @Override
    public Boolean exists(AnalystDTO analystDTO){
      List<User> userList=userRepository.findAll();
        List<AnalystDTO> analystDTOList= dtoMapper.UserListToAnalyst(userList);
        for(AnalystDTO analystDTO1:analystDTOList){
            if (analystDTO1.getUserName().equals(analystDTO.getUserName())){
            return true;
            }
        }return false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    if(updatedUser.getUserName()!=null){     user.setUserName(updatedUser.getUserName());}
                    if(updatedUser.getProjects()!=null){    user.setProjects(updatedUser.getProjects());}
                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(Long id) {
        User user= userRepository.findById(id).get();
       return user;
    }
    @Override
    public List<Document> getDocumentsByUser(Long id){
        User user=userRepository.findById(id).get();
        List<Document> documents=new ArrayList<>();
        if(user.getProjects()!=null){   List<Project> projects=user.getProjects();
            for (Project project : projects) {
                documents.addAll(project.getDocuments());
            }
            return documents;}

     return null;
    }
    @Override
    public List<Project> getProjectsByUser(Long id){
        User user = userRepository.findById(id).get();
        List<Project> projects=user.getProjects();
        return projects;
    }
    @Override
    public String authenticate(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User fullUser = userRepository.findByUserName(user.getUserName());

            return jwtService.generateToken(fullUser.getId(), fullUser.getUserName());
        }
        return "Not Authenticated";
    }

}

//This is boring stuff , I'll use dtomapper for better mapping and stuff IDK
//I choose you kakarot to be the first of my VICTIMS