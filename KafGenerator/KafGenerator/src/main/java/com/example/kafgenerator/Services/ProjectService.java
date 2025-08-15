package com.example.kafgenerator.Services;
import com.example.kafgenerator.DTO.ProjectDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Repositories.ProjectRepository;
import com.example.kafgenerator.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    GenerateTextFromTextInput generateTextFromTextInput;

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
        Project project = projectRepository.findById(id).get();
        ProjectDTO projectDTO = dtoMapper.projectToDTO(project);
        return project;

    }

    @Override
    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    if (updatedProject.getName() != null) {
                        project.setName(updatedProject.getName());
                    }
                    if (updatedProject.getDocuments() != null) {
                        project.setDocuments(updatedProject.getDocuments());
                    }
                    if (updatedProject.getUser() != null) {
                        project.setUser(updatedProject.getUser());
                    }
                    if (updatedProject.getDescription() != null) {
                        project.setDescription(updatedProject.getDescription());
                    }
                    if (updatedProject.getCreatedAt() != null) {
                        project.setCreatedAt(updatedProject.getCreatedAt());
                    }
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<ProjectDTO> getAllProjects() {

        List<ProjectDTO> projectDTOS = dtoMapper.projectListToDTO(projectRepository.findAll());
        return projectDTOS;
    }


    //setproject method twali n3ayetlha automatically kol mauser yzid project

    @Override
    public Project setProjectToUser(Long id, Long idp) {

        Project project = projectRepository.findById(idp).get();
        if (project.getUser() == null) {
            User user = userRepository.findById(id).get();
            project.setUser(user);
            user.getProjects().add(project);
            userRepository.save(user);
            projectRepository.save(project);
            return project;
        }
        return null;
    }


    public String generateReport(Long id) {
        Project project = projectRepository.findById(id).get();
        List<Document> documents = project.getDocuments();

        // Concatenate all document content into a single string
        String allDocumentsContent = documents.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n---\n\n")); // Use a clear separator


        String prompt2 ="""
You are an expert compliance analyst for a financial institution. Your task is to evaluate a client based on the provided conditions and documents. Provide a comprehensive report that first gives a brief overview of the findings, then validates each condition with a PASS/FAIL status, citing specific proof from the documents. Finally, conclude with a final ACCEPT or REJECT decision.

**Acceptance Conditions:**
- **Condition 1: Financial Solvency:** The company must have a positive Net Income for the most recent fiscal year.
- **Condition 2: Corporate Structure:** All financial statements must be signed off by a certified auditor from an independent third-party firm.
- **Condition 3: Related-Party Disclosures:** The company's financial statements must clearly disclose any related-party transactions over $50,000.

**Client Information:**
Client Name: %s
Project Description: %s

**Documents Content:**
%s

**Report Format:**
**Compliance Report for [Client Name]**

**1. Executive Summary:**
[A brief, high-level summary of the analysis findings and overall recommendation.]

**2. Condition-Based Analysis:**
**Condition 1: Financial Solvency**
[PASS/FAIL] - [Brief explanation and proof from the document, e.g., "The Income Statement shows a Net Income of $320,000 for 2025, which is a positive value."].

**Condition 2: Corporate Structure**
[PASS/FAIL] - [Brief explanation and proof from the document, e.g., "The Independent Auditor's Report is signed by Deloitte & Touche LLP, a certified third-party firm."].

**Condition 3: Related-Party Disclosures**
[PASS/FAIL] - [Brief explanation and proof from the document, e.g., "The Notes to Financial Statements disclose a $75,000 related-party transaction, which is over the $50,000 threshold."].

**3. Final Conclusion:**
[ACCEPT/REJECT] - [Brief summary of the overall findings].
""".formatted(project.getName(), project.getDescription(), allDocumentsContent);


        String response = generateTextFromTextInput.askGemini(prompt2);
       /* String response = aiService.chat(prompt);
           return response;

       }*/
        return response;
    }
}








