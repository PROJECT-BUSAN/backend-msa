package project.profileservice.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBadgeDto {
    private String name;
    private String image_url;

    public UpdateBadgeDto(String name, String image_url) {
        this.name = name;
        this.image_url = image_url;
    }
}
