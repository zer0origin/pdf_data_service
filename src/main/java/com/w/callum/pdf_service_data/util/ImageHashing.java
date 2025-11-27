package com.w.callum.pdf_service_data.util;

import com.sun.jna.Native;

public class ImageHashing {
    private static final ImageHashingGoLangBindings imageHashingGoLangBindings;
    static {
        imageHashingGoLangBindings = Native.load("hashing", ImageHashingGoLangBindings.class);
    }
    public String hashPageOfDocumentString(String encodedData){
        return imageHashingGoLangBindings.hashPageOfDocumentString(encodedData);
    }
}