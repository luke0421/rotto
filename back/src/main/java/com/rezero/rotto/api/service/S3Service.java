package com.rezero.rotto.api.service;

import com.rezero.rotto.dto.dto.S3Dto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {

    S3Dto getPresignedUrlToUpload(long userCode, String fileName, String mimeType);

    String saveFileToS3(String path, MultipartFile multipartFile) throws IOException;

    byte[] downloadFile(String key);

    void saveByteToS3(String key, byte[] fileBytes);
}