package com.recipe.book.web.domain.comment;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.recommendation.Recommendation;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.global.config.security.Ownable;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
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
public class Comment implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(updatable = false, nullable = false)
    private User writer;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String principalName;

    public static Comment create(Recipe recipe, User writer, String content) {
        return Comment.builder()
                .recipe(recipe)
                .writer(writer)
                .content(content)
                .build();
    }

    public Comment update(String content) {
        this.content = content;
        return this;
    }
}
