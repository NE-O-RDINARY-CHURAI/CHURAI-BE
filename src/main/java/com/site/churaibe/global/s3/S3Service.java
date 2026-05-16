package com.site.churaibe.global.s3;

import com.site.churaibe.global.apiPayload.code.S3ErrorCode;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFiles(String directory, List<MultipartFile> files) {
        List<String> uploadedUrls = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String fileName = directory + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            log.debug("[S3Service] 파일 업로드 시도: {}", fileName);
            
            try (InputStream inputStream = file.getInputStream()) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
                
                String url = s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build()).toString();
                
                log.info("[S3Service] 파일 업로드 성공: {}", url);
                uploadedUrls.add(url);
            } catch (IOException e) {
                log.error("[S3Service] 파일 업로드 실패: {}", fileName, e);
                throw new GeneralException(S3ErrorCode.S3_UPLOAD_FAILED);
            }
        }
        
        return uploadedUrls;
    }
}
