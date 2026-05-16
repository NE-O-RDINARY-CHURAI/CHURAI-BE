package com.site.churaibe.domain.reaction.exception.code.error;

import com.site.churaibe.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReactionErrorCode implements BaseErrorCode {
    REACTION_POST_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "REACTION404_1",
        "게시글을 찾을 수 없습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
