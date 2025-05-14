package com.rpsgame.utils;

import java.util.Base64;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;

public class ImageUtil {

    public static String saveBase64Image(String base64Image, String namePrefix) throws Exception {
        String cleanedBase64 = base64Image.split(",")[1]; // remove data:image/jpeg;base64,
        byte[] decodedBytes = Base64.getDecoder().decode(cleanedBase64);

        File file = File.createTempFile(namePrefix, ".jpg");
        try (OutputStream stream = new FileOutputStream(file)) {
            stream.write(decodedBytes);
        }

        return file.getAbsolutePath();
    }
}
