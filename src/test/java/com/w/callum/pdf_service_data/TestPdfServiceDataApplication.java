package com.w.callum.pdf_service_data;

import org.springframework.boot.SpringApplication;

public class TestPdfServiceDataApplication {

    public static void main(String[] args) {
        SpringApplication.from(PdfServiceDataApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
