package com.example.movie_manager.dto;

import com.example.movie_manager.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewView {
    private Long id;
    private String nickname;
    private Integer score;
    private String content;
    private LocalDateTime createdAt;

    public static ReviewView fromEntity(Review r) {
        return ReviewView.builder()
                .id(r.getId())
                .nickname(r.getMember().getNick()) // ✅ Member.nick
                .score(r.getScore())
                .content(r.getContent())
                .createdAt(r.getCreatedAt())
                .build();
    }
}