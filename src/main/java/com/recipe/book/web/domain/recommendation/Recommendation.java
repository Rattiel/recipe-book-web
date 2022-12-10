package com.recipe.book.web.domain.recommendation;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.user.User;
import com.recipe.book.web.global.config.security.Ownable;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Recommendation implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String principalName;

    public static Recommendation create(User owner, Recipe recipe) {
        return Recommendation.builder()
                .owner(owner)
                .recipe(recipe)
                .build();
    }
}
