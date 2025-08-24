package com.example.kafgenerator.Services;
import com.example.kafgenerator.DTO.ProjectDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Produit;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Repositories.ProduitRepository;
import com.example.kafgenerator.Repositories.ProjectRepository;
import com.example.kafgenerator.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class ProjectService implements IProjectService {
    @Autowired
    ProduitRepository produitRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DtoMapper dtoMapper;
    @Autowired
    private AiService aiService;
    @Autowired
    GeminiAiService geminiAiService;

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

    //setproject method twali n3ayetlha automatically kol ma user yzid project

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

    @Override
    public String generateReport(Long projectId, Long produitId) throws IOException, InterruptedException {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Produit produit = produitRepository.findById(produitId).orElseThrow();
        List<Document> documents = project.getDocuments();

        // Concatenate all documents
        String allDocumentsContent = documents.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n---\n\n"));

        // Build JSON input for Python
        Map<String, Object> inputData = Map.of(
                "documents", allDocumentsContent,
                "conditions", produit.getConditions(),
                "client_name", project.getName(),
                "project_name", project.getDescription()
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInput = objectMapper.writeValueAsString(inputData);

        // Load Python script from same folder
        ClassLoader classLoader = getClass().getClassLoader();
        File scriptFile = new File(classLoader.getResource("TEST.py").getFile());

        ProcessBuilder pb = new ProcessBuilder("py", "-3.12", scriptFile.getAbsolutePath());

        pb.redirectErrorStream(true); // merge stderr into stdout
        Process process = pb.start();

        // Write JSON input and close to signal EOF
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(jsonInput);
            writer.flush();
            writer.close(); // important to avoid Python hanging on stdin.read()
        }

        // Read Python output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script exited with code " + exitCode + "\nOutput:\n" + output.toString());
        }

        return output.toString().trim();
    }

/*
    public String generateReport(Long id, Long idprod) {
        // Fetch project and product
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
        Produit produit = produitRepository.findById(idprod).orElseThrow(() -> new RuntimeException("Produit not found"));
        List<Document> documents = project.getDocuments();

        // Concatenate all documents with a clear separator
        String allDocumentsContent = documents.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n---\n\n"));

        // Get current date for comparisons
        String today = java.time.LocalDate.now().toString(); // Format: YYYY-MM-DD

        // Build the prompt
        String prompt2 = """
You are an expert compliance analyst for a financial institution. Evaluate the client strictly based on the provided conditions and documents.

**Acceptance Conditions:**
%s

**Client Information:**
Client Name: %s
Project Description: %s
Client Type: %s
Client Individual Type (if applicable): %s
Current Date: %s

**Documents Content:**
%s

**Instructions:**
- Only evaluate conditions relevant to the applicant's type:
  - Salaryman: Conditions 1, 2, 5, 8, 9
  - Retired: Conditions 1, 3, 6, 8, 9
  - Professional/Self-employed: Conditions 1, 4, 7, 8, 9
- Skip all conditions that are not applicable.
- Output each applicable condition in the exact one-line format provided in the JSON output_format.
- Include only the proof necessary to justify PASS or FAIL.
- Provide a brief Final Conclusion.
- Use the current date for any duration calculations.
""".formatted(
                produit.getConditions(),
                project.getName(),
                project.getDescription(),
                project.getClientType(),
                project.getIndividualType() != null ? project.getIndividualType() : "",
                today,
                allDocumentsContent
        );
        String response = GeminiAiService.askGemini(prompt2);
        return response;
    }

*/
}








