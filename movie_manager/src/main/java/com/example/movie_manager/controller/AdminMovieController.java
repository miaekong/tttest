package com.example.movie_manager.controller;

import com.example.movie_manager.dto.MovieForm;
import com.example.movie_manager.entity.Movie;
import com.example.movie_manager.service.MovieService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class AdminMovieController {

    private final MovieService movieService;

    private boolean isAdmin(HttpSession session) {
        Object role = session.getAttribute("loginRole");
        return role != null && role.toString().equals("ADMIN");
    }

    @GetMapping("/admin")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        model.addAttribute("countMovies", movieService.countActiveMovies());
        model.addAttribute("countFeatured", movieService.countFeaturedMovies());
        return "admin/dashboard";
    }

    @GetMapping("/admin/movies/new")
    public String createForm(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("movieForm", new MovieForm());
        model.addAttribute("mode", "create");
        return "admin/movie_form";
    }

    @PostMapping("/admin/movies/new")
    public String create(@Valid @ModelAttribute MovieForm movieForm,
                         BindingResult br,
                         @RequestParam(required = false) MultipartFile poster,
                         HttpSession session,
                         Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        if (br.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/movie_form";
        }

        String saved = movieService.savePoster(poster);
        Movie movie = movieForm.toEntity(saved);
        movieService.create(movie);
        return "redirect:/movies";
    }

    @GetMapping("/admin/movies/{id}/edit")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        Movie movie = movieService.findById(id);
        if (movie == null) return "redirect:/movies";

        model.addAttribute("movieForm", MovieForm.fromEntity(movie));
        model.addAttribute("existingPoster", movie.getPosterFileName());
        model.addAttribute("mode", "edit");
        return "admin/movie_form";
    }

    @PostMapping("/admin/movies/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute MovieForm movieForm,
                       BindingResult br,
                       @RequestParam(required = false) MultipartFile poster,
                       HttpSession session,
                       Model model) {
        if (!isAdmin(session)) return "redirect:/login";

        Movie existing = movieService.findById(id);
        if (existing == null) return "redirect:/movies";

        if (br.hasErrors()) {
            model.addAttribute("existingPoster", existing.getPosterFileName());
            model.addAttribute("mode", "edit");
            return "admin/movie_form";
        }

        String posterFile = existing.getPosterFileName();
        String newPoster = movieService.savePoster(poster);
        if (newPoster != null) posterFile = newPoster;

        Movie updated = movieForm.toEntity(posterFile);
        updated.setId(id);

        movieService.update(updated);
        return "redirect:/movies/" + id;
    }

    @PostMapping("/admin/movies/{id}/delete")
    public String delete(@PathVariable Long id) {
        movieService.archive(id);   // ✅ 소프트 삭제
        return "redirect:/movies";
    }
}