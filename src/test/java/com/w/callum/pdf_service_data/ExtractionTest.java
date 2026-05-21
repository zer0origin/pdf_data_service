package com.w.callum.pdf_service_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.w.callum.pdf_service_data.controller.BasicRoutes;
import com.w.callum.pdf_service_data.extraction.ExtractionTextStripper;
import com.w.callum.pdf_service_data.model.Coordinate;
import com.w.callum.pdf_service_data.model.ExtractionRequest;
import com.w.callum.pdf_service_data.model.Selection;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class ExtractionTest {
    @Test
    public void ExtractionTestOne() {
        File encodedDocument = new File("src/test/java/com/w/callum/pdf_service_data/invoice_Phillip Breyer_11502.txt");
        try (FileInputStream fileInputStream = new FileInputStream(encodedDocument)) {
            byte[] data = fileInputStream.readAllBytes();
            String encodedData = new String(data, StandardCharsets.UTF_8);
            byte[] decodedDocument = Base64.getDecoder().decode(encodedData);

            PDDocument document = Loader.loadPDF(decodedDocument);
            Coordinate coordinate = new Coordinate(0, 612, 0, 792);
            ExtractionTextStripper extractionTextStripper = new ExtractionTextStripper(coordinate);
            extractionTextStripper.setPageStart("0");
            extractionTextStripper.setPageEnd("0");
            extractionTextStripper.getText(document);

            Assertions.assertFalse(extractionTextStripper.getStrippedData().isEmpty());
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void EndpointTest() {
        String selectionUUID1 = "d643b48d-1601-473e-b74f-3778d4b2448f";
        String selectionUUID2 = "1191d3b6-135a-43f8-a258-bcb22c3405c3";
        BasicRoutes basicRoutes = new BasicRoutes();

        File encodedDocument = new File("src/test/java/com/w/callum/pdf_service_data/invoice_Phillip Breyer_11502.txt");
        try (FileInputStream fileInputStream = new FileInputStream(encodedDocument)) {
            byte[] data = fileInputStream.readAllBytes();
            String encodedData = new String(data, StandardCharsets.UTF_8);

            Coordinate coordinate1 = new Coordinate(0, 612, 0, 792);
            Coordinate coordinate2 = new Coordinate(0, 612, 0, 792);

            Selection selection1 = new Selection(selectionUUID1, selectionUUID1, coordinate1, "HGlrO8tOC2P");
            Selection selection2 = new Selection(selectionUUID2, selectionUUID2, coordinate2, "HGlrO8tOC2P");

            basicRoutes.getExtractData(new ExtractionRequest("", encodedData, Map.of(selection1.selectionUUID(), selection1, selection2.selectionUUID(), selection2))).subscribe(o -> {
                System.out.println("Response received");

                //TODO - Change to not use a map in a map, i really do not like this.
                ResponseEntity<Map<String, Map<Double, List<ExtractionTextStripper.TextData>>>> receivedData = (ResponseEntity<Map<String, Map<Double, List<ExtractionTextStripper.TextData>>>>) o;
                Assertions.assertNotNull(receivedData.getBody());
                System.out.println(receivedData.getBody().size());

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                try {
                    String json = ow.writeValueAsString(receivedData.getBody());
                    System.out.println(json);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}