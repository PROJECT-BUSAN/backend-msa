package project.profileservice.api.t;


import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.profileservice.domain.Badge;
import project.profileservice.service.BadgeService;
import project.profileservice.domain.UpdateBadgeDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class BadgeApiController {

    private final BadgeService badgeService;

    @GetMapping("/badge")
    @ApiOperation(value = "뱃지목록 조회", notes = "전체 뱃지종류를 모두 조회한다.")
    public List<BadgeResult> BadgeListV1() {
        List<Badge> badgeList = badgeService.findAllBadge();
        List<BadgeResult> result = badgeList.stream()
                .map(b -> new BadgeResult(b))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/badge/{id}")
    @ApiOperation(value = "뱃지 조회", notes = "id에 맞는 뱃지를 조회한다.")
    public BadgeResult BadgeV1(@PathVariable("id") Long badge_id) {
        Badge badge = badgeService.findOne(badge_id);
        BadgeResult result = new BadgeResult(badge);
        return result;
    }

    @PostMapping("/badge")
    @ApiOperation(value = "뱃지 생성", notes = "새로운 뱃지를 생성한다.")
    public CreateBadgeResponse createBadgeV1(@RequestBody CreateBadgeRequest request) {
        Long badge_id = badgeService.create(request.getName(), request.getImage_url());
        return new CreateBadgeResponse(badge_id);
    }

    @PutMapping("/badge/{id}")
    @ApiOperation(value = "뱃지수정", notes = "id에 맞는 뱃지를 수정한다.")
    public UpdateBadgeResponse updateBadgeV1(@PathVariable("id") Long badge_id, 
                                             @RequestBody UpdateBadgeRequest request) {
        badgeService.update(badge_id, new UpdateBadgeDto(request.getName(), request.getImage_url()));
        Badge updatedBadge = badgeService.findOne(badge_id);
        return new UpdateBadgeResponse(updatedBadge.getId(), updatedBadge.getName(), updatedBadge.getImage_url());
    }

    @DeleteMapping("/badge/{id}")
    @ApiOperation(value = "뱃지삭제", notes = "id에 맞는 뱃지를 삭제한다.")
    public ResponseEntity deleteBadgeV1(@PathVariable("id") Long badge_id) {
        badgeService.delete(badge_id);
        return new ResponseEntity<>(new Message("삭제되었습니다."), HttpStatus.OK);
                
    }

    @Data
    @AllArgsConstructor
    static class Message {
        private String message; 
    }

    @Data
    static class BadgeResult {
        private Long id;
        private String name;
        private String image_url;

        public BadgeResult(Badge badge) {
            this.id = badge.getId();
            this.name = badge.getName();
            this.image_url = badge.getImage_url();
        }
    }

    @Data
    static class CreateBadgeRequest{
        private String name;
        private String image_url;
    }
    
    @Data
    static class UpdateBadgeRequest{
        private String name;
        private String image_url;
    }

    @Data
    @AllArgsConstructor
    static class CreateBadgeResponse {
        private Long id;
    }
    @Data
    @AllArgsConstructor
    static class UpdateBadgeResponse {
        private Long id;
        private String name;
        private String image_url;
    }
}
