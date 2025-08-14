package com.example.kafgenerator.Services;
import com.example.kafgenerator.DTO.ProjectDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Repositories.ProjectRepository;
import com.example.kafgenerator.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class ProjectService implements IProjectService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DtoMapper dtoMapper;
    @Autowired
    private AiService aiService;

    @Override
    public ProjectDTO addProject(ProjectDTO projectDTO) {
         Project project = dtoMapper.dtoToProject(projectDTO);
         projectRepository.save(project);
        return projectDTO;
    }
    //remove mch lezem dto hawka tool bel id
    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    @Override
    public Project getProject(Long id) {
        Project project= projectRepository.findById(id).get();
        ProjectDTO projectDTO= dtoMapper.projectToDTO(project);
        return project   ;

    }

    @Override
    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    if(updatedProject.getName()!=null){     project.setName(updatedProject.getName());}
                    if(updatedProject.getDocuments()!=null){   project.setDocuments(updatedProject.getDocuments());}
                    if (updatedProject.getUser() != null) {project.setUser(updatedProject.getUser());}
                    if (updatedProject.getDescription() != null) {project.setDescription(updatedProject.getDescription());}
                    if(updatedProject.getCreatedAt()!=null){project.setCreatedAt(updatedProject.getCreatedAt());}
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    @Override
    public List<ProjectDTO> getAllProjects() {

        List<ProjectDTO> projectDTOS =dtoMapper.projectListToDTO(projectRepository.findAll());
         return projectDTOS;
    }


    //setproject method twali n3ayetlha automatically kol mauser yzid project

    @Override
    public Project setProjectToUser(Long id, Long idp) {

        Project project = projectRepository.findById(idp).get();
        if(project.getUser()==null) {
            User user = userRepository.findById(id).get();
            project.setUser(user);
            user.getProjects().add(project);
            userRepository.save(user);
            projectRepository.save(project);
            return project;}
return null;
       }

       
       @Override
       public String generateReport(Project project) {

        String prompt = "what's the name of this project %s: ".formatted(project.getName()) ;
        String prompt2 = """
You are an expert compliance analyst for a financial institution. 
Your task is to evaluate whether a company should be accepted as a client 
based strictly on the provided conditions.

STRICT RULES:
1. Follow the provided conditions exactly; do not invent or interpret new rules.
2. For each condition, clearly state PASS or FAIL.
3. For each FAIL, explain why in detail using evidence from the documents.
4. Do not include irrelevant commentary.
5. If any required information is missing, mark the condition as FAIL and note the missing data.

Here are the acceptance conditions:
[INSERT YOUR EXACT CONDITIONS HERE]

Client Name: %s
Project Description: %s
Documents Content:
%s
TASK: Evaluate this client against the provided conditions and produce a detailed report.
The report must contain:
- A table with Condition | PASS/FAIL | Explanation
- A final conclusion: ACCEPT or REJECT

                """.formatted(project.getName(),project.getDescription(),project.getDocuments());


         String response = aiService.chat(prompt);
           return response;

       }

        }








