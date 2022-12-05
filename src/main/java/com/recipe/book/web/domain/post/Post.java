package com.recipe.book.web.domain.post;

import com.recipe.book.web.domain.member.User;
import com.recipe.book.web.global.config.security.Ownable;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post implements Ownable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private List<String> thumbnails;

    @Column(nullable = false)
    private Integer viewsCount;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(updatable = false)
    private User owner;

    @Formula("(SELECT p.id FROM Post p WHERE p.id > id ORDER BY p.id asc LIMIT 1)")
    private Long after;

    @Formula("(SELECT p.id FROM Post p WHERE p.id < id ORDER BY p.id desc LIMIT 1)")
    private Long before;

    public boolean isModified() {
        return createDate != modifiedDate;
    }

    public static Post create(String title, List<String> thumbnails, String content) {
        return Post.builder()
                .title(title)
                .thumbnails(thumbnails)
                .content(content)
                .viewsCount(0)
                .build();
    }

    public Post update(String title, String content) {
        this.title = title;
        this.content = content;

        return this;
    }

    public Post look() {
        // this.viewsCount++;

        return this;
    }
}
