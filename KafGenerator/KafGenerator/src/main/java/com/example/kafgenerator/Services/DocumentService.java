package com.example.kafgenerator.Services;
import com.example.kafgenerator.DTO.DocumentDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Project;
import com.example.kafgenerator.Entities.Type;
import com.example.kafgenerator.Repositories.DocumentRepository;
import com.example.kafgenerator.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService implements IDocumentService {
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    DtoMapper dtoMapper;



    public Document addDocument(String name, Type type, MultipartFile file, Long projectId) throws IOException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Document document = new Document();
        document.setName(name);
        document.setType(type);
        document.setContent(file.getBytes());
        document.setProject(project);

        return documentRepository.save(document);
    }


    @Override
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
    @Override
    public List<DocumentDTO> getAllDocuments() {
        List<Document> documents = documentRepository.findAll();
        List<DocumentDTO> documentsdto=dtoMapper.documentListToDTO(documents);
        return  documentsdto;
}
    @Override
    public Document getDocument(Long id) {
        Document document = documentRepository.findById(id).get();
        return document;
    }
    @Override
    public Document setDocumentToProject(Long idp, Long idd){
Document document = documentRepository.findById(idd).get();
if(document.getProject() == null){
    Project project=projectRepository.findById(idp).get();
    document.setProject(project);
    projectRepository.save(project);
    project.getDocuments().add(document);
    documentRepository.save(document);
    return document;
}
return null;

    }


@Override
public List<Document> getDocumentsByProjectId(Long idp){
List<Document> documents =projectRepository.findById(idp).get().getDocuments();



return documents;}



}
