package com.example.kafgenerator.Controller;
import com.example.kafgenerator.DTO.AnalystDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Services.IUserSevice;
import com.example.kafgenerator.Services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {
    @Autowired
    JwtService jwtService;
    @Autowired
    IUserSevice userService;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.authenticate(user);
    }

    @PostMapping("/add")
    public AnalystDTO addAnalyst(@RequestBody AnalystDTO analystDTO)
    {
 return userService.registerUser(analystDTO);
    }

    @GetMapping("/get/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteAnalyst(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody User user) {
        return userService.updateUser(id,user);
    }

    @GetMapping("/get/{id}")
    public User getUser(@PathVariable Long id) {
  return userService.getUser(id);
    }

    @GetMapping("getdocs")
    public List<Document> getdocs(@RequestParam Long id) {
        return userService.getDocumentsByUser(id);
 }

 @GetMapping("/getprojects")
    public List<Project> getprojects(@RequestParam  Long id) {
        return userService.getProjectsByUser(id);
 }

}
