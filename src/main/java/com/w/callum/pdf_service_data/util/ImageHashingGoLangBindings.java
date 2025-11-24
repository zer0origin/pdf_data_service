package com.w.callum.pdf_service_data.util;

import com.sun.jna.*;

public interface ImageHashingGoLangBindings extends Library {
    String hashPageOfDocumentString(String cStr);
}