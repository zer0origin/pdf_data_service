package com.w.callum.pdf_service_data.extraction;

import com.w.callum.pdf_service_data.controller.BasicRoutes;
import com.w.callum.pdf_service_data.model.Coordinate;
import com.w.callum.pdf_service_data.model.ExtractionRequest;
import com.w.callum.pdf_service_data.model.Selection;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TestExtraction {
    @Test
    public void RealDocumentFindPage2() throws IOException {
        File encodedDocument = new File("src/test/java/com/w/callum/pdf_service_data/extraction/DHL 31 - Invoice");
        FileInputStream fileInputStream = new FileInputStream(encodedDocument);
        byte[] encodedDocumentData = fileInputStream.readAllBytes();

        byte[] decodedDocument = Base64.getDecoder().decode(encodedDocumentData);
        PDDocument document = Loader.loadPDF(decodedDocument);

        String keyToFind = "JU8BIB4TLeQ";
        HashKeyPage hashKeyPage = new HashKeyPage(keyToFind, document);
        hashKeyPage.setDpi(72);
        hashKeyPage.getPageUsingKey();
        hashKeyPage.getPageIfFound().ifPresentOrElse(pdPage -> {}, Assertions::fail);

        keyToFind = "1AmubdO287V";
        hashKeyPage = new HashKeyPage(keyToFind, document);
        hashKeyPage.setDpi(72);
        hashKeyPage.getPageUsingKey();
        hashKeyPage.getPageIfFound().ifPresentOrElse(pdPage -> {}, Assertions::fail);

        keyToFind = "2fHRTFZpQtp";
        hashKeyPage = new HashKeyPage(keyToFind, document);
        hashKeyPage.setDpi(72);
        hashKeyPage.getPageUsingKey();
        hashKeyPage.getPageIfFound().ifPresentOrElse(pdPage -> {}, Assertions::fail);
    }

    @Test
    public void ExtractEndpointWithRealDocument() throws IOException {
        File encodedDocument = new File("src/test/java/com/w/callum/pdf_service_data/extraction/DHL 31 - Invoice");
        FileInputStream fileInputStream = new FileInputStream(encodedDocument);
        byte[] encodedDocumentData = fileInputStream.readAllBytes();

        BasicRoutes routes = new BasicRoutes();
        /**
         *         "selectionCoordinate": {
         *           "x1": 66.0,
         *           "x2": 239.0,
         *           "y1": 117.0,
         *           "y2": 196.0
         *         }
         */

        Map<String, Selection> selections = new HashMap<>(1);
        selections.put("abc", new Selection("abc", "123", new Coordinate(66f, 239f, 117f, 196f), "JU8BIB4TLeQ"));
        Mono<?> extractData = routes.getExtractData(new ExtractionRequest("123", new String(encodedDocumentData), selections));
        extractData.subscribe(o -> {
            System.out.println(o);
        });
    }
}
