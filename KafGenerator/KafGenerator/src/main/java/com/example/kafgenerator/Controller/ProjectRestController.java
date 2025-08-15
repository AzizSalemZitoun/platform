package com.example.kafgenerator.Controller;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Repositories.ProjectRepository;
import com.example.kafgenerator.Services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectRestController {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    IProjectService projectService;
    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        Project saved = projectRepository.save(project);
        return ResponseEntity.ok(saved);
    }
    @PutMapping("/update/{id}")
    public Project updateProject(@PathVariable Long id,@RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @GetMapping("/get/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }
    @PostMapping("/setproject")
    public Project setProject(@RequestParam Long id,@RequestParam Long idp) {
      return projectService.setProjectToUser(id,idp);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
@PostMapping("/generate/{id}")
    public String generateProject(@PathVariable Long id){
   return projectService.generateReport(id);


}
}
