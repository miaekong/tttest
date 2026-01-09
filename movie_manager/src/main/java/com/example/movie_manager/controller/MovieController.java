package com.example.movie_manager.controller;

import com.example.movie_manager.dto.MovieView;
import com.example.movie_manager.dto.ReviewForm;
import com.example.movie_manager.dto.ReviewView;
import com.example.movie_manager.entity.Movie;
import com.example.movie_manager.entity.Review;
import com.example.movie_manager.service.MovieService;
import com.example.movie_manager.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ReviewService reviewService;

    @GetMapping("/movies")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String q,
                       Model model) {

        Page<Movie> result = movieService.search(q, page, size);

        model.addAttribute("movies", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("q", q);
        model.addAttribute("currentPage", result.getNumber());
        model.addAttribute("totalPages", result.getTotalPages());

        return "movie/list";
    }

    @GetMapping("/movies/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Movie movie = movieService.findById(id);
        if (movie == null) return "redirect:/movies";

        List<Review> reviews = reviewService.listByMovie(id);

        model.addAttribute("movie", MovieView.fromEntity(movie));
        model.addAttribute("reviews", reviews.stream().map(ReviewView::fromEntity).toList());
        model.addAttribute("reviewForm", new ReviewForm());
        model.addAttribute("isLogin", session.getAttribute("loginId") != null);

        return "movie/detail";
    }
}