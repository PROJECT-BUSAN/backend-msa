package project.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.repository.BadgeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    // Id에 맞는 뱃지 가져오기
    public Badge findOne(Long badge_id) {
        return badgeRepository.findOne(badge_id);
    }
    
    // 현재 저장된 모든 뱃지 가져오기
    public List<Badge> findAllBadge() {
        return badgeRepository.findAll();
    }

    // 새로운 뱃지 생성
    @Transactional
    public Long create(String name, String image_url) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setImage_url(image_url);
        badgeRepository.save(badge);
        return badge.getId();
    }

    // 뱃지 수정
    @Transactional
    public void update(Long badge_id, UpdateBadgeDto badgeDto) {
        Badge badge = badgeRepository.findOne(badge_id);
        badge.setName(badgeDto.getName());
        badge.setImage_url(badgeDto.getImage_url());
    }

    // 뱃지 삭제
    @Transactional
    public void delete(Long badge_id) {
        badgeRepository.deleteById(badge_id);
    }
    
}
