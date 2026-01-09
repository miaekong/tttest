package com.example.movie_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "SEQ_REVIEW_GEN",
        sequenceName = "SEQ_REVIEW",
        allocationSize = 1
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REVIEW_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    /**
     * ✅ A안(가장 빠름)
     * DB의 REVIEW 테이블에 USER_ID NOT NULL 컬럼이 남아있으므로
     * 엔티티 매핑을 user_id로 맞춰서 NULL 삽입 오류(ORA-01400)를 막는다.
     *
     * 주의: 타입은 Member를 쓰되, 컬럼명만 user_id로 유지한다.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")   // ✅ DB 컬럼명 그대로 사용
    private Member member;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

