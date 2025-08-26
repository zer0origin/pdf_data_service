package com.w.callum.pdf_service_data.controller;

import com.w.callum.pdf_service_data.model.request.PostImageRequest;
import com.w.callum.pdf_service_data.model.request.PostMetaRequest;
import com.w.callum.pdf_service_data.model.response.PostMetaResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
public class BasicRoutes {

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
                List<String> encodedArr = new ArrayList<>(noOfPages);
                meta = new PostMetaResponse(height, width, noOfPages, encodedArr);
                for (int i = 0; i < noOfPages; i++) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 300); // 300 DPI
                        ImageIO.write(bufferedImage, "png", outputStream);

                        byte[] imageBytes = outputStream.toByteArray();
                        String image = Base64.getUrlEncoder().encodeToString(imageBytes);
                        encodedArr.add(image);
                    }
                }
            } catch (IOException e) {
                monoSink.error(e);
                return;
            }

            monoSink.success(ResponseEntity.ok().body(meta));
        }).timeout(Duration.ofSeconds(15));
    }

    @PostMapping("/extract")
    public Mono<?> getExtractData() {
        return Mono.just(ResponseEntity.badRequest().build());
    }
}
