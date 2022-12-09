package com.recipe.book.web.domain.recommendation;

import com.recipe.book.web.domain.recipe.Recipe;
import com.recipe.book.web.domain.user.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@IdClass(RecommendationId.class)
public class Recommendation {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;
}
