// [경로] src/main/java/com/example/movie_manager/service/ReviewService.java
package com.example.movie_manager.service;

import com.example.movie_manager.entity.Member;
import com.example.movie_manager.entity.Movie;
import com.example.movie_manager.entity.Review;
import com.example.movie_manager.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepo;

    public List<Review> listByMovie(Long movieId) {
        // ✅ fetch join 버전 사용
        return reviewRepo.findByMovieIdWithMember(movieId);
    }

    public Review addReview(Movie movie, Member member, String content, Integer score) {
        Review r = Review.builder()
                .movie(movie)
                .member(member)
                .content(content)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();
        return reviewRepo.save(r);
    }
}
