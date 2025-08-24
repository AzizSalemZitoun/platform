package com.example.kafgenerator.Services;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;


public class DocumentParser {


    public String parseDocx(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             XWPFDocument document = new XWPFDocument(is)) {

            StringBuilder text = new StringBuilder();
            // Iterate through all paragraphs in the document
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText());
                text.append("\n"); // Add a newline after each paragraph
            }
            return text.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error parsing DOCX file: " + e.getMessage();
        }
    }

    public String parseXlsx(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            StringBuilder text = new StringBuilder();
            // Iterate through all sheets in the workbook
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();
    
                // Iterate through each row of the sheet
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    // Iterate through each cell of the row
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case STRING:
                                text.append(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                text.append(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                text.append(cell.getBooleanCellValue());
                                break;
                            case FORMULA:

                                text.append(cell.getStringCellValue());
                                break;
                            default:
                                text.append(" ");
                        }
                        text.append("\t"); // Add a tab after each cell for better readability
                    }
                    text.append("\n"); // Add a newline after each row
                }
            }
            return text.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error parsing XLSX file: " + e.getMessage();
        }
    }

    /**
     * Parses the text content from a PDF file uploaded as a MultipartFile.
     * It uses Apache PDFBox to strip all text from the document.
     *
     * @param file The uploaded PDF file.
     * @return A String containing the parsed text, or an error message if parsing fails.
     */
    public String parsePdf(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            // Create a PDFTextStripper to extract the text
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(document);

        } catch (IOException e) {
            e.printStackTrace();
            return "Error parsing PDF file: " + e.getMessage();
        }
    }



    public String parseImage(MultipartFile file) {
        return "Text parsing from images is not supported by this class. You may need to use a dedicated OCR library.";
    }
}
