package com.site.churaibe.domain.post.entity;

import com.site.churaibe.global.entity.BaseEntity;
import com.site.churaibe.domain.image.entity.PostImage;
import com.site.churaibe.domain.tag.entity.Tag;

import com.site.churaibe.domain.reaction.entity.Reaction;

import com.site.churaibe.domain.comment.entity.Comment;
import com.site.churaibe.domain.post.enums.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @Column(nullable = false, length = 64)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private Long views = 0L;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();

    @Builder
    public Post(String title, String contents, String nickname, Category category, String password) {
        this.title = title;
        this.contents = contents;
        this.nickname = nickname;
        this.category = category;
        this.password = password;
        this.views = 0L;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.confirmPost(this);
    }

    public void addPostImage(PostImage postImage) {
        this.postImages.add(postImage);
        postImage.confirmPost(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.confirmPost(this);
    }

    public void addReaction(Reaction reaction) {
        this.reactions.add(reaction);
        reaction.confirmPost(this);
    }
}
