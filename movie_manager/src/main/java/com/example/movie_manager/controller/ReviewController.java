// [경로] src/main/java/com/example/movie_manager/controller/ReviewController.java
package com.example.movie_manager.controller;

import com.example.movie_manager.dto.ReviewForm;
import com.example.movie_manager.entity.Member;
import com.example.movie_manager.entity.Movie;
import com.example.movie_manager.repository.MemberRepository;
import com.example.movie_manager.service.MovieService;
import com.example.movie_manager.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final MemberRepository memberRepo; // ✅ Member 사용

    @PostMapping("/movies/{id}/reviews")
    public String add(@PathVariable Long id,
                      @Valid @ModelAttribute ReviewForm reviewForm,
                      BindingResult br,
                      HttpSession session) {

        Long loginId = (Long) session.getAttribute("loginId");
        if (loginId == null) return "redirect:/login";

        Movie movie = movieService.findById(id);

        if (br.hasErrors()) {
            return "redirect:/movies/" + id;
        }

        Member member = memberRepo.findById(loginId).orElse(null);
        if (member == null) return "redirect:/login";

        reviewService.addReview(movie, member, reviewForm.getContent(), reviewForm.getScore());
        return "redirect:/movies/" + id;
    }
}
