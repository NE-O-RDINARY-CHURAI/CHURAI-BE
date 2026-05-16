package com.site.churaibe.domain.post.repository;

import com.site.churaibe.domain.post.entity.Post;
import com.site.churaibe.domain.post.enums.Category;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.contents LIKE %:keyword% ORDER BY p.createdAt DESC")
    List<Post> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:keyword% OR p.contents LIKE %:keyword%) AND p.category = :category ORDER BY p.createdAt DESC")
    List<Post> searchByKeywordAndCategory(@Param("keyword") String keyword, @Param("category") Category category);

    @Query("SELECT p FROM Post p LEFT JOIN p.reactions r GROUP BY p ORDER BY COUNT(r) DESC")
    List<Post> findRanking(Pageable pageable);
}
