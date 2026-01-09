// [경로] src/main/java/com/example/movie_manager/entity/Movie.java
package com.example.movie_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "movie")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "SEQ_MOVIE_GEN",
        sequenceName = "SEQ_MOVIE",
        allocationSize = 1
)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MOVIE_GEN")
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Column(length=100)
    private String genre;

    private LocalDate releaseDate;

    private Integer runningTime;

    @Column(length=30)
    private String rating;

    @Column(length=100)
    private String director;

    @Column(length=400)
    private String actors;

    @Column(length=1000)
    private String synopsis;

    @Column(length=300)
    private String posterFileName;

    @Column(nullable=false)
    private boolean featured;

    @Column(nullable=false)
    private boolean archived;
}
