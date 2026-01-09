package com.example.movie_manager.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewForm {

    @NotBlank
    private String content;

    @Min(1) @Max(5)
    private Integer score;
}