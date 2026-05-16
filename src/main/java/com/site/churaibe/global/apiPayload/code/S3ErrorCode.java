package com.site.churaibe.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorCode implements BaseErrorCode {
    S3_UPLOAD_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "S3_500_1",
        "S3 파일 업로드 중 오류가 발생했습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
