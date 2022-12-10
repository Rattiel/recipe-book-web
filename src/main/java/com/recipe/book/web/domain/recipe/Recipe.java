package com.recipe.book.web.domain.recipe;

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
public class Recipe implements Ownable {
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
    private Long views;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @Formula(
            "(" +
                    "SELECT count(*) " +
                    "FROM Recommendation rec " +
                    "WHERE rec.recipe_id = id" +
            ")"
    )
    private Long recommendationCount;

    @OneToMany(
            mappedBy = "recipe",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Recommendation> recommendations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, nullable = false)
    private User writer;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String principalName;

    @Formula(
            "(" +
                    "SELECT r.id " +
                    "FROM Recipe r " +
                    "WHERE r.id > id " +
                    "ORDER BY r.id asc LIMIT 1" +
            ")"
    )
    private Long after;

    @Formula(
            "(" +
                    "SELECT r.id " +
                    "FROM Recipe r " +
                    "WHERE r.id < id " +
                    "ORDER BY r.id desc LIMIT 1" +
            ")"
    )
    private Long before;

    public static Recipe withId(Long id) {
        return Recipe.builder()
                .id(id)
                .build();
    }

    public static Recipe create(String title, User writer, List<String> thumbnails, String content) {
        return Recipe.builder()
                .title(title)
                .writer(writer)
                .thumbnails(thumbnails)
                .content(content)
                .views(0L)
                .build();
    }

    public Recipe update(String title, List<String> thumbnails, String content) {
        this.title = title;
        this.content = content;
        this.thumbnails = thumbnails;
        return this;
    }
}
