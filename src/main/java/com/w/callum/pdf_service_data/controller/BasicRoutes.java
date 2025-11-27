package com.w.callum.pdf_service_data.controller;

import com.w.callum.pdf_service_data.model.request.PostMetaRequest;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;

@RestController
public class BasicRoutes {

    static {
        System.out.printf("Using DPI of %.2f\n", Env.getEnvOrDefault("DOCUMENT_DPI",Float::parseFloat, 72.0f));
    }

    @PostMapping(path = "/meta", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Object> getMetaData(@RequestBody PostMetaRequest dataRequest) {
        return Mono.create(monoSink -> {
            float height, width;
            int noOfPages;
            PostMetaResponse meta;
            byte[] decodedDocument = Base64.getDecoder().decode(dataRequest.getBase64());
            try (PDDocument document = Loader.loadPDF(decodedDocument)) {
                noOfPages = document.getNumberOfPages();
                PDPage documentPage = document.getPage(0);

                PDRectangle rec = documentPage.getMediaBox();
                height = rec.getHeight();
                width = rec.getWidth();

                PDFRenderer pdfRenderer = new PDFRenderer(document);
                Map<String, byte[]> encodedArr = new HashMap<>(noOfPages);
                HashSet<String> keys = new HashSet<>(noOfPages);
                meta = new PostMetaResponse(height, width, encodedArr);
                for (int i = 0; i < noOfPages; i++) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, Env.getEnvOrDefault("DOCUMENT_DPI",Float::parseFloat, 72.0f));
                        ImageIO.write(bufferedImage, "png", outputStream);

                        ImageHashing imageHashing = new ImageHashing();
                        byte[] imageBytes = outputStream.toByteArray();
                        String imageString = Base64.getEncoder().encodeToString(imageBytes);

                        String key = imageHashing.hashPageOfDocumentString(imageString);

                        if (keys.contains(key)) { //Duplication checking
                            continue;
                        } else {
                            keys.add(key);
                            encodedArr.put(key, imageBytes);
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
        record Result(String message){}
        return Mono.just(new Result("Pong!"));
    }

    @PostMapping("/extract")
    public Mono<?> getExtractData() {
        return Mono.just(ResponseEntity.badRequest().build());
    }
}
