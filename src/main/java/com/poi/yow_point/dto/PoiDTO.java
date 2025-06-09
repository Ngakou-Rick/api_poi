package com.poi.yow_point.dto; // Adaptez le package

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Data Transfer Object for Point of Interest")
public class PoiDTO {

    @Schema(description = "Unique identifier of the Point of Interest", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID poiId;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255)
    @Schema(description = "Name of the Point of Interest", requiredMode = Schema.RequiredMode.REQUIRED, example = "Musée National")
    private String name;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100)
    @Schema(description = "Country where the PoI is located", requiredMode = Schema.RequiredMode.REQUIRED, example = "Cameroun")
    private String country;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100)
    @Schema(description = "City where the PoI is located", requiredMode = Schema.RequiredMode.REQUIRED, example = "Yaoundé")
    private String city;

    @Size(max = 1000)
    @Schema(description = "Detailed description of the Point of Interest", example = "Un lieu historique majeur...")
    private String description;

    @NotBlank(message = "Category cannot be blank")
    @Schema(description = "Category of the Point of Interest", requiredMode = Schema.RequiredMode.REQUIRED, example = "museum")
    private String category;

    @NotNull(message = "Latitude cannot be null")
    @Schema(description = "Latitude coordinate of the PoI", requiredMode = Schema.RequiredMode.REQUIRED, example = "3.866667")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    @Schema(description = "Longitude coordinate of the PoI", requiredMode = Schema.RequiredMode.REQUIRED, example = "11.516667")
    private Double longitude;

    @NotBlank(message = "Address cannot be blank")
    @Schema(description = "Formal address of the PoI", example = "Rue de Narvik, Yaoundé")
    private String address;

    @Size(max = 500)
    @Schema(description = "Informal or descriptive address (optional)", example = "Près du rond-point de la poste")
    private String informalAddress;

    @Schema(description = "List of items in the PoI's catalogue", example = "[\"Plat du jour\", \"Exposition temporaire\"]")
    private List<String> catalogue = new ArrayList<>();

    @Schema(description = "Opening hours of the PoI", example = "[\"Lundi-Vendredi: 09h-18h\", \"Samedi: 10h-16h\"]")
    private List<String> openingHours = new ArrayList<>();

    @Schema(description = "Contact information", example = "+237 222 22 00 00")
    private String contactInfo;

    @Schema(description = "Official website URL", example = "https://www.musee-national.cm")
    private String website;

    // Constructeur optionnel
    public PoiDTO(UUID poiId, String name, String country, String city, String description, String category,
            Double latitude, Double longitude, String address, String informalAddress,
            List<String> catalogue, List<String> openingHours, String contactInfo, String website) {
        this.poiId = poiId;
        this.name = name;
        this.country = country;
        this.city = city;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.informalAddress = informalAddress;
        this.catalogue = catalogue;
        this.openingHours = openingHours;
        this.contactInfo = contactInfo;
        this.website = website;
    }
}