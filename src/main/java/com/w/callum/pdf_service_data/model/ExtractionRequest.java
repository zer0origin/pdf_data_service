package com.w.callum.pdf_service_data.model;

import java.util.Map;
import java.util.UUID;

public record ExtractionRequest(String documentUid, String base64EncodedDocument, Map<String, Selection> selections) {
}
