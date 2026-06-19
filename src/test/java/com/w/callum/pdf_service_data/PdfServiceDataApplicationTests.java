package com.w.callum.pdf_service_data;

import com.w.callum.pdf_service_data.extraction.HashKeyPage;
import com.w.callum.pdf_service_data.util.ImageHashing;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;

class PdfServiceDataApplicationTests {

}