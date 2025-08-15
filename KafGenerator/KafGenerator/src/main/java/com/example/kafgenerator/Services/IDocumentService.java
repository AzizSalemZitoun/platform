package com.example.kafgenerator.Services;

import com.example.kafgenerator.DTO.DocumentDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Type;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface IDocumentService {



    //public Document addDocument(String name, Type type, MultipartFile file, Long projectId)throws IOException;

    Document addDocument(String name, Type type, String content, Long projectId);

    void deleteDocument(Long id);

    List<DocumentDTO> getAllDocuments();

    Document getDocument(Long id);

    Document setDocumentToProject(Long idp, Long iddoc);

    List<Document> getDocumentsByProjectId(Long idp);

}
