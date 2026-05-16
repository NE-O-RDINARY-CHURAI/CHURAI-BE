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

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        String key = extractKeyFromUrl(fileUrl);
        log.debug("[S3Service] S3 파일 삭제 시도: {}", key);

        try {
            s3Client.deleteObject(builder -> builder.bucket(bucket).key(key));
            log.info("[S3Service] S3 파일 삭제 성공: {}", key);
        } catch (Exception e) {
            // S3 파일 삭제 실패가 비즈니스 트랜잭션 전체를 롤백시키지 않도록 경고 로그만 남김
            log.warn("[S3Service] S3 파일 삭제 실패 (DB 트랜잭션 안전성 유지를 위해 예외를 생략하고 계속 진행합니다): {}", key, e);
        }
    }

    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) return;
        fileUrls.forEach(this::deleteFile);
    }

    private String extractKeyFromUrl(String fileUrl) {
        String delimiter = "amazonaws.com/";
        int index = fileUrl.indexOf(delimiter);
        if (index != -1) {
            return fileUrl.substring(index + delimiter.length());
        }
        return fileUrl;
    }
}
