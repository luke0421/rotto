package com.rezero.rotto.api.controller;

import com.rezero.rotto.api.service.S3Service;
import com.rezero.rotto.dto.dto.S3Dto;
import com.rezero.rotto.utils.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
@Tag(name = "S3 컨트롤러", description = "S3 사용을 위한 API")
public class S3Controller {

    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;

    // presigned url 요청 api
    // 파일 종류
    // 녹음 m4a, mp3, wav
    // 이미지 jpeg, jpg, png
    // 외부모델 pth, index
    @Operation(summary = "S3에 파일 업로드", description = "Presigned URL 을 사용하여 파일 업로드")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/upload")
    public ResponseEntity<?> getPresignedUrlToUpload(@RequestHeader("Authorization") String token,
                                                     @RequestParam(value = "fileName") String fileName,
                                                     @RequestParam(value = "mimeType") String mimeType,
                                                     @RequestParam(value = "fileSize") long fileSize) throws IOException {

        long userCode = Long.parseLong(jwtTokenProvider.getPayload(token.substring(7)));

        final long maxSize = 10 * 1024 * 1024; // 10MB
        if (fileSize > maxSize){
            return ResponseEntity.badRequest().body("File size exceeds the limit of 10MB.");
        }

        S3Dto response = s3Service.getPresignedUrlToUpload(userCode, fileName, mimeType);

        return ResponseEntity.ok(response);
    }

}