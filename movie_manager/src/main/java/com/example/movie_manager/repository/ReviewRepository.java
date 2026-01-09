// [경로] src/main/java/com/example/movie_manager/repository/ReviewRepository.java
package com.example.movie_manager.repository;

import com.example.movie_manager.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // ✅ member를 fetch join으로 같이 로딩해서 LazyInitializationException 방지
    @Query("""
        select r
        from Review r
        join fetch r.member
        where r.movie.id = :movieId
        order by r.createdAt desc
    """)
    List<Review> findByMovieIdWithMember(@Param("movieId") Long movieId);
}
