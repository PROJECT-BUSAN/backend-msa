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

    /**
     * user_id에 맞는 프로필의 모든 정보를 조회한다. 
     * @param user_id
     * @return
     */
    @GetMapping("/{user_id}")
    public ProfileResponse ProfileAllInfoV1(@PathVariable("user_id") Long user_id) {
        Profile profile = profileService.findOneAllInfo(user_id);
        return new ProfileResponse(profile);
    }

    /**
     * 새로운 프로필을 생성한다
     * (회원가입 시 1회 호출될 것)
     * @param request
     * @return
     */
    @PostMapping("")
    public CreateProfileResponse createProfileV1(@RequestBody @Valid CreateProfileRequest request){
        Long user_id = profileService.create(request.getUser_id());
        return new CreateProfileResponse(user_id);
    }

    /**
     * 프로필을 삭제한다.
     * (회원탈퇴 시 호출될 것)
     * @param user_id
     * @return
     */
    @DeleteMapping("/{user_id}")
    public Message deleteProfileV1(@PathVariable("user_id") Long user_id){
        profileService.delete(user_id);
        return new Message("삭제되었습니다.");
    }

    /**
     * 특정 유저가 특정 뱃지를 획득한다.
     * @param user_id
     * @param badge_id
     * @return
     */
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
        private String image_url;

        public BadgeDto(Badge badge) {
            this.id = badge.getId();;
            this.name = badge.getName();
            this.image_url = badge.getImage_url();
        }
    }
    
    @Data
    static class ProfileResponse {
        private Long profile_id;
        private Long user_id;
        private int nowStrick;
        private int maxStrick;
        private List<BadgeDto> badges = new ArrayList<>();

        public ProfileResponse(Profile profile) {
            this.profile_id = profile.getId();
            this.user_id = profile.getUser_id();
            this.nowStrick = profile.getNowStrick();
            this.maxStrick = profile.getMaxStrick();
            for(ProfileBadge profileBadge : profile.getProfileBadges()) {
                badges.add(new BadgeDto(profileBadge.getBadge()));
            }
        }
    }
}
