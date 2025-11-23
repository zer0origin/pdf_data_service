package com.w.callum.pdf_service_data.util;

import com.sun.jna.*;

import java.util.Arrays;
import java.util.List;

public interface ImageHashing extends Library {
    String hashPageOfDocumentString(String cStr);
}