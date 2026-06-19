package com.w.callum.pdf_service_data.extraction;

import com.w.callum.pdf_service_data.util.Env;
import com.w.callum.pdf_service_data.util.ImageHashing;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

public class HashKeyPage {
    private final PDDocument document;
    private final String key;
    private boolean found = false;
    private int pageIndex;
    private PDPage page;
    private float dpi = Env.getEnvOrDefault("DOCUMENT_DPI", Float::parseFloat, 72.0f);

    public HashKeyPage(String key, PDDocument document) {
        this.key = key;
        this.document = document;
    }

    public void getPageUsingKey() {
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, dpi);
                ImageIO.write(bufferedImage, "png", outputStream);
                byte[] imageBytes = outputStream.toByteArray();
                ImageHashing imageHashing = new ImageHashing();

                long numberFNV1A = imageHashing.ConvertByteArrToNumberFNV1A(imageBytes);
                long hashedNumber = imageHashing.Hash64shift(numberFNV1A);
                String currentKey = imageHashing.ConvertHashToString(hashedNumber);

                if (key.equals(currentKey)) {
                    pageIndex = i;
                    found = true;
                    page = document.getPage(i);

                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            pageIndex = -1;
        }
    }

    public void getPageUsingDatabase() {
        throw new NotImplementedException("TODO: getPageUsingDatabase"); //TODO: getPageUsingDatabase
    }

    public Optional<PDPage> getPageIfFound() {
        if (pageIndex == 0) {
            getPageUsingKey();
        }

        if (pageIndex == -1 || page == null) {
            return Optional.empty();
        }

        return Optional.of(page);
    }

    public void setDpi(float dpi) {
        this.dpi = dpi;
    }

    public boolean isFound() {
        return found;
    }

    public int getPageIndex() {
        return pageIndex;
    }
}
