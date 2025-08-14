package com.example.kafgenerator.Controller;
import com.example.kafgenerator.DTO.DocumentDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Type;
import com.example.kafgenerator.Services.DtoMapper;
import com.example.kafgenerator.Services.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/document")
public class DocumentRestController {
   @Autowired
    DtoMapper dtoMapper;
    @Autowired
    IDocumentService documentService;

    @PostMapping("/add")
    public ResponseEntity<DocumentDTO> addDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("type") Type type,
            @RequestParam("projectId") Long projectId
    ) {
        try {
            System.out.println(file.getOriginalFilename()+" type : "+file.getContentType());
           if(file.getContentType().equals("application/pdf")) {
               type=Type.pdf;
               Document document = documentService.addDocument(name, type, file, projectId);
               return ResponseEntity.ok(dtoMapper.documentToDTO(document));
           } else if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
               type=Type.docx;
               Document document = documentService.addDocument(name, type, file, projectId);
               return ResponseEntity.ok(dtoMapper.documentToDTO(document));
           }
           else if(file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
               type=Type.excel;
               Document document = documentService.addDocument(name, type, file, projectId);
               return ResponseEntity.ok(dtoMapper.documentToDTO(document));
           }else if(file.getContentType().equals("image/jpg")) {
               type=Type.excel;
               Document document = documentService.addDocument(name, type, file, projectId);
               return ResponseEntity.ok(dtoMapper.documentToDTO(document));
           }
       return null;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long id) {
        Document document = documentService.getDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .body(document.getContent());
    }
    @PostMapping("/setdocument")
    public Document setDocument(@RequestParam Long idp, @RequestParam Long idd) {
        return documentService.setDocumentToProject(idp, idd);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }

    @GetMapping("/getByProject")
    public ResponseEntity<List<Document>> getDocumentsByProjectId(@RequestParam Long id){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .body(documentService.getDocumentsByProjectId(id));
    }

}
