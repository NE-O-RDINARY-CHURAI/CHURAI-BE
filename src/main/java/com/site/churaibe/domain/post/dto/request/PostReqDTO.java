package com.site.churaibe.domain.post.dto.request;

import com.site.churaibe.domain.post.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

public class PostReqDTO {
    @Builder
    public record PostSaveDTO(
        @NotBlank(message = "제목은 필수 입력 사항입니다.")
        @Schema(description = "게시글 제목", example = "츄라이 츄라이")
        String title,

        @NotBlank(message = "내용은 필수 입력 사항입니다.")
        @Schema(description = "게시글 내용", example = "여기 진짜 맛있어요!")
        String contents,

        @Schema(description = "작성자 닉네임", example = "미식가")
        String nickname,

        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        @Schema(description = "수정/삭제용 비밀번호", example = "1234")
        String password,

        @NotNull(message = "카테고리는 필수 입력 사항입니다.")
        @Schema(description = "카테고리", example = "MAIN_DISH")
        Category category,

        @Size(max = 3, message = "이미지는 최대 3장까지만 업로드 가능합니다.")
        @Schema(description = "이미지 파일 목록", type = "array", implementation = String.class, format = "binary")
        List<MultipartFile> images
    ) {
    }

    public record PostUpdateDTO(
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password,
        String title,
        String contents,
        Category category,
        List<String> imageUrls
    ) {}
}
