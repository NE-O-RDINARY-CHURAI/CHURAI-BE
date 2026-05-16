package com.site.churaibe.domain.post.service.query;

import com.site.churaibe.domain.post.converter.PostConverter;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.exception.PostErrorCode;
import com.site.churaibe.domain.post.repository.PostRepository;
import com.site.churaibe.global.apiPayload.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;

    // category가 주어지면 해당 카테고리 게시글만, 없으면 전체를 최신순으로 반환
    public List<PostResDTO.PostSummaryDTO> getPosts(Category category) {
        List<Post> posts = category != null
            ? postRepository.findByCategoryOrderByCreatedAtDesc(category)
            : postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(PostConverter::toSummaryDTO).toList();
    }

    // 조회수를 증가시키므로 readOnly=false로 오버라이드
    @Transactional
    public PostResDTO.PostDetailDTO getPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new GeneralException(PostErrorCode.POST_NOT_FOUND));
        post.incrementViews();
        return PostConverter.toDetailDTO(post);
    }

    // 총 리액션 수(츄라이 + 흥미) 기준 상위 10개 게시글 반환
    public List<PostResDTO.PostSummaryDTO> getRanking() {
        return postRepository.findRanking(PageRequest.of(0, 10))
            .stream().map(PostConverter::toSummaryDTO).toList();
    }

    // 제목 또는 내용에 keyword가 포함된 게시글 검색, category로 추가 필터링 가능
    public List<PostResDTO.PostSummaryDTO> searchPosts(String keyword, Category category) {
        List<Post> posts = category != null
            ? postRepository.searchByKeywordAndCategory(keyword, category)
            : postRepository.searchByKeyword(keyword);
        return posts.stream().map(PostConverter::toSummaryDTO).toList();
    }
}
