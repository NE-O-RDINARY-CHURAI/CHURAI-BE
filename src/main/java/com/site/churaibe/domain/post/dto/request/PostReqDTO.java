package com.site.churaibe.domain.post.dto.request;

import com.site.churaibe.domain.post.enums.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

public class PostReqDTO {
    @Builder
    public record PostSaveDTO (
        @NotBlank(message = "제목은 필수 입력 사항입니다.")
        String title,
        @NotBlank(message = "내용은 필수 입력 사항입니다.")
        String contents,
        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        String nickname,
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password,
        Category category,
        List<String> imageUrls
    ){
    }

    public record PostUpdateDTO(
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password,
        String title,
        String contents,
        Category category,
        List<String> imageUrls,
        List<String> tags
    ) {}
}
