package com.site.churaibe.domain.reaction.exception.code.success;

import com.site.churaibe.global.apiPayload.code.BaseSuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReactionSuccessCode implements BaseSuccessCode {
    REACTION_SAVE_SUCCESS(
        HttpStatus.OK,
        "REACTION200_1",
        "리액션이 성공적으로 추가되었습니다."
    ),
    REACTION_GET_SUCCESS(
        HttpStatus.OK,
        "REACTION200_2",
        "리액션 조회에 성공했습니다."
    ),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
