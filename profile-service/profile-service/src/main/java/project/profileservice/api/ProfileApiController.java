package project.profileservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.profileservice.domain.Badge;
import project.profileservice.domain.Profile;
import project.profileservice.domain.ProfileBadge;
import project.profileservice.service.ProfileService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileApiController {

    private final ProfileService profileService;

    @GetMapping("/{user_id}")
    public ProfileResponse ProfileV1(@PathVariable("user_id") Long user_id) {
        Profile profile = profileService.findOne(user_id);
        return new ProfileResponse(profile);
    }
    
    @PostMapping("")
    public CreateProfileResponse createProfileV1(@RequestBody @Valid CreateProfileRequest request){
        System.out.println("START============");
        Long user_id = profileService.create(request.getUser_id());
        System.out.println("END==============");
        return new CreateProfileResponse(user_id);
    }
    
    @DeleteMapping("/{user_id}")
    public Message deleteProfileV1(@PathVariable("user_id") Long user_id){
        profileService.delete(user_id);
        return new Message("삭제되었습니다.");
    }

    @PostMapping("{user_id}/badge/{badge_id}")
    public Message ProfileGetBadge(@PathVariable("user_id") Long user_id, @PathVariable("badge_id") Long badge_id) {
        profileService.obtainBadge(user_id, badge_id);
        return new Message("획득 성공");
    }


    @Data
    @AllArgsConstructor
    static class Message {
        private String message;
    }
    
    @Data
    static class CreateProfileRequest {
        @NotNull
        private Long user_id;
    }

    @Data
    @AllArgsConstructor
    static class CreateProfileResponse {
        private Long user_id;
    }

    static class BadgeDto {
        private Long id;
        private String name;

        public BadgeDto(Badge badge) {
            this.id = badge.getId();;
            this.name = badge.getName();
            this.image_url = badge.getImage_url();
        }

        private String image_url;
    }
    
    @Data
    static class ProfileResponse {
        private Long profile_id;
        private Long user_id;
        private int strick;
        private List<BadgeDto> badges = new ArrayList<>();

        public ProfileResponse(Profile profile) {
            this.profile_id = profile.getId();
            this.user_id = profile.getUser_id();
            this.strick = profile.getStrick();
            for(ProfileBadge profileBadge : profile.getProfileBadges()) {
                badges.add(new BadgeDto(profileBadge.getBadge()));
            }
        }
    }
}
