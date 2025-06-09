package com.poi.yow_point.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;

@Data
public class ReviewDTO {
    @NotNull
    private UUID poiId;
    // userId sera pris du Principal (utilisateur authentifié)

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 2000)
    private String reviewText;
}