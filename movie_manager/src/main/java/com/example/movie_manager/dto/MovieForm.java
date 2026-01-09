package com.example.movie_manager.dto;

import com.example.movie_manager.entity.Movie;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class MovieForm {

    private Long id;

    @NotBlank
    private String title;

    private String genre;
    private LocalDate releaseDate;
    private Integer runningTime;
    private String rating;
    private String director;
    private String actors;
    private String synopsis;

    private boolean featured;
    private boolean archived;

    public Movie toEntity(String posterFileName) {
        return Movie.builder()
                .id(id)
                .title(title)
                .genre(genre)
                .releaseDate(releaseDate)
                .runningTime(runningTime)
                .rating(rating)
                .director(director)
                .actors(actors)
                .synopsis(synopsis)
                .posterFileName(posterFileName)
                .featured(featured)
                .archived(archived)
                .build();
    }

    public static MovieForm fromEntity(Movie m) {
        MovieForm f = new MovieForm();
        f.setId(m.getId());
        f.setTitle(m.getTitle());
        f.setGenre(m.getGenre());
        f.setReleaseDate(m.getReleaseDate());
        f.setRunningTime(m.getRunningTime());
        f.setRating(m.getRating());
        f.setDirector(m.getDirector());
        f.setActors(m.getActors());
        f.setSynopsis(m.getSynopsis());
        f.setFeatured(m.isFeatured());
        f.setArchived(m.isArchived());
        return f;
    }
}