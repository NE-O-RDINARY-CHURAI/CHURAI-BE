package com.site.churaibe.domain.post.service.query;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.post.dto.response.PostResDTO;
import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.Category;
import com.site.churaibe.domain.post.enums.ReactionType;
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
        return posts.stream().map(this::toSummaryDTO).toList();
    }

    // 조회수를 증가시키므로 readOnly=false로 오버라이드
    @Transactional
    public PostResDTO.PostDetailDTO getPost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new GeneralException(PostErrorCode.POST_NOT_FOUND));
        post.incrementViews();
        return toDetailDTO(post);
    }

    // 총 리액션 수(츄라이 + 흥미) 기준 상위 10개 게시글 반환
    public List<PostResDTO.PostSummaryDTO> getRanking() {
        return postRepository.findRanking(PageRequest.of(0, 10))
            .stream().map(this::toSummaryDTO).toList();
    }

    // 제목 또는 내용에 keyword가 포함된 게시글 검색, category로 추가 필터링 가능
    public List<PostResDTO.PostSummaryDTO> searchPosts(String keyword, Category category) {
        List<Post> posts = category != null
            ? postRepository.searchByKeywordAndCategory(keyword, category)
            : postRepository.searchByKeyword(keyword);
        return posts.stream().map(this::toSummaryDTO).toList();
    }

    // 목록용 요약 DTO 변환 (썸네일은 첫 번째 이미지)
    private PostResDTO.PostSummaryDTO toSummaryDTO(Post post) {
        String thumbnail = post.getPostImages().isEmpty() ? null : post.getPostImages().get(0).getImageUrl();
        List<String> tags = post.getTags().stream().map(t -> t.getName()).toList();
        long churai = post.getReactions().stream().filter(r -> r.getType() == ReactionType.CHURAI).count();
        long interested = post.getReactions().stream().filter(r -> r.getType() == ReactionType.INTERESTED).count();
        return new PostResDTO.PostSummaryDTO(
            post.getId(),
            post.getTitle(),
            post.getNickname(),
            post.getCategory(),
            post.getViews(),
            post.getCreatedAt(),
            thumbnail,
            tags,
            churai,
            interested,
            post.getComments().size()
        );
    }

    // 상세 DTO 변환 (최상위 댓글만 필터링 후 대댓글 재귀 포함)
    private PostResDTO.PostDetailDTO toDetailDTO(Post post) {
        List<String> imageUrls = post.getPostImages().stream().map(i -> i.getImageUrl()).toList();
        List<String> tags = post.getTags().stream().map(t -> t.getName()).toList();
        long churai = post.getReactions().stream().filter(r -> r.getType() == ReactionType.CHURAI).count();
        long interested = post.getReactions().stream().filter(r -> r.getType() == ReactionType.INTERESTED).count();
        List<PostResDTO.CommentResDTO> comments = post.getComments().stream()
            .filter(c -> c.getParent() == null)
            .map(this::toCommentDTO)
            .toList();
        return new PostResDTO.PostDetailDTO(
            post.getId(),
            post.getTitle(),
            post.getContents(),
            post.getNickname(),
            post.getCategory(),
            post.getViews(),
            post.getCreatedAt(),
            imageUrls,
            tags,
            churai,
            interested,
            comments
        );
    }

    // 댓글을 DTO로 변환, children을 재귀적으로 replies에 포함
    private PostResDTO.CommentResDTO toCommentDTO(Comment comment) {
        List<PostResDTO.CommentResDTO> replies = comment.getChildren().stream()
            .map(this::toCommentDTO)
            .toList();
        return new PostResDTO.CommentResDTO(
            comment.getId(),
            comment.getContents(),
            comment.getCreatedAt(),
            replies
        );
    }
}
