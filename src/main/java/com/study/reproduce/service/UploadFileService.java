package com.study.reproduce.service;

import java.io.IOException;
import java.io.InputStream;

public interface UploadFileService {

    String SimpleUploadFileFromStream(String key, InputStream inputStream) throws IOException;

    String SimpleUploadFileFromLocal(String key, String filePath);
}
