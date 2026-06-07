package com.w.callum.pdf_service_data.controller;

import com.w.callum.pdf_service_data.extraction.ExtractionTextStripper;
import com.w.callum.pdf_service_data.extraction.HashKeyPage;
import com.w.callum.pdf_service_data.model.Coordinate;
import com.w.callum.pdf_service_data.model.ExtractionRequest;
import com.w.callum.pdf_service_data.model.Selection;
import com.w.callum.pdf_service_data.model.request.MetaRequest;
import com.w.callum.pdf_service_data.model.response.PostMetaResponse;
import com.w.callum.pdf_service_data.util.Env;
import com.w.callum.pdf_service_data.util.ImageHashing;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.time.Duration;
import java.util.*;

@RestController
public class BasicRoutes {

    static {
        System.out.printf("Using DPI of %.2f\n", Env.getEnvOrDefault("DOCUMENT_DPI", Float::parseFloat, 72.0f));
    }

    @PostMapping(path = "/meta", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Object> getMetaData(@RequestBody MetaRequest data) {
        return Mono.create(monoSink -> {
            float height, width;
            int noOfPages;
            PostMetaResponse meta;
            String encodedData = data.getBase64();
            byte[] decodedDocument = Base64.getDecoder().decode(encodedData);
            try (PDDocument document = Loader.loadPDF(decodedDocument)) {
                noOfPages = document.getNumberOfPages();
                PDPage documentPage = document.getPage(0);

                PDRectangle rec = documentPage.getMediaBox();
                height = rec.getHeight();
                width = rec.getWidth();

                PDFRenderer pdfRenderer = new PDFRenderer(document);
                Map<String, String> encodedArr = new HashMap<>(noOfPages);
                HashSet<String> keys = new HashSet<>(noOfPages);
                meta = new PostMetaResponse(height, width, encodedArr);

                for (int i = 0; i < noOfPages; i++) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, Env.getEnvOrDefault("DOCUMENT_DPI", Float::parseFloat, 72.0f));
                        ImageIO.write(bufferedImage, "png", outputStream);
                        byte[] imageBytes = outputStream.toByteArray();
                        String pageEncoded = Base64.getEncoder().encodeToString(imageBytes);

                        ImageHashing imageHashing = new ImageHashing();
                        long numberFNV1A = imageHashing.ConvertByteArrToNumberFNV1A(imageBytes);
                        long hashedNumber = imageHashing.Hash64shift(numberFNV1A);
                        String key = imageHashing.ConvertHashToString(hashedNumber);

                        if (key.equals("1AmubdO287V")){
                            System.out.println("KEY DIAGNOSIS: ");
                            System.out.println(numberFNV1A);
                            System.out.println(hashedNumber);
                            System.out.println(key);
                            System.out.println("KEY DIAGNOSIS END");
                        }

                        if (keys.contains(key)) {
                            continue;
                        } else {
                            keys.add(key);
                            encodedArr.put(key, pageEncoded);
                        }

                        meta.setNoOfPages(keys.size());
                    }
                }
            } catch (IOException e) {
                monoSink.error(e);
                return;
            }

            monoSink.success(ResponseEntity.ok().body(meta));
        }).timeout(Duration.ofSeconds(20));
    }

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Object> getPing() {
        record Result(String message) {
        }
        return Mono.just(new Result("Pong!"));
    }

    @PostMapping("/extract")
    public Mono<?> getExtractData(@RequestBody ExtractionRequest data) { //TODO: Allow the caller to provide the hashkey to page table, this will speed up the extraction significantly.
        Map<String, Map<Double, List<ExtractionTextStripper.TextData>>> result = new HashMap<>();

        for (Selection selection : data.selections().values()) {
            Coordinate coordinate = selection.coordinates();
            byte[] bytes = Base64.getDecoder().decode(data.base64EncodedDocument().getBytes(StandardCharsets.UTF_8));
            try (PDDocument document = Loader.loadPDF(bytes)) {
                HashKeyPage hashKeyPage = new HashKeyPage(selection.pageKey(), document);
                hashKeyPage.getPageUsingKey();
                hashKeyPage.getPageIfFound().ifPresent(pdPage -> {
                    ExtractionTextStripper extractionTextStripper = new ExtractionTextStripper(coordinate);
                    extractionTextStripper.setStartPage(hashKeyPage.getPageIndex() + 1);
                    extractionTextStripper.setEndPage(hashKeyPage.getPageIndex() + 1);

                    try {
                        extractionTextStripper.getText(document);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Map<Double, List<ExtractionTextStripper.TextData>> strippedData = extractionTextStripper.getStrippedData();
                    result.put(selection.selectionUUID(), strippedData);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Mono.just(ResponseEntity.ok(result));
    }
}
