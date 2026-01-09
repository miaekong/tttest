package com.example.movie_manager.dto;

import com.example.movie_manager.entity.Movie;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MovieView {
    private Long id;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    private Integer runningTime;
    private String rating;
    private String director;
    private String actors;
    private String synopsis;
    private String posterUrl;
    private boolean featured;
    private boolean archived;

    public static MovieView fromEntity(Movie m) {
        return MovieView.builder()
                .id(m.getId())
                .title(m.getTitle())
                .genre(m.getGenre())
                .releaseDate(m.getReleaseDate())
                .runningTime(m.getRunningTime())
                .rating(m.getRating())
                .director(m.getDirector())
                .actors(m.getActors())
                .synopsis(m.getSynopsis())
                .posterUrl(m.getPosterFileName() == null ? null : ("/uploads/" + m.getPosterFileName()))
                .featured(m.isFeatured())
                .archived(m.isArchived())
                .build();
    }
}