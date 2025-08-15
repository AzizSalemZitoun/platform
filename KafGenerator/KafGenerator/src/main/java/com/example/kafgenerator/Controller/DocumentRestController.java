package com.example.kafgenerator.Controller;
import com.example.kafgenerator.DTO.DocumentDTO;
import com.example.kafgenerator.Entities.Document;
import com.example.kafgenerator.Entities.Type;
import com.example.kafgenerator.Services.DocumentParser;
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

        DocumentParser documentParser = new DocumentParser();
        String content = "";

        try {
            System.out.println(file.getOriginalFilename() + " type : " + file.getContentType());

            if (file.getContentType().equals("application/pdf")) {
                type = Type.pdf;
                content = documentParser.parsePdf(file);
            } else if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                type = Type.docx;
                // hedha lel word
                content = documentParser.parseDocx(file);
            } else if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                type = Type.excel;
                // ehdha lel exceeel
                content = documentParser.parseXlsx(file);
            } else {
                // SNN CIAO
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }

            Document document = documentService.addDocument(name, type, content, projectId);

            return ResponseEntity.ok(dtoMapper.documentToDTO(document));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @GetMapping("/get/{id}")
    public ResponseEntity<String> getDocument(@PathVariable Long id) {
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
