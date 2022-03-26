package project.profileservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.profileservice.domain.Badge;
import project.profileservice.domain.dto.UpdateBadgeDto;
import project.profileservice.repository.BadgeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;

    /**
     * id에 맞는 뱃지를 가져온다.
     * @param badge_id
     * @return
     */
    public Badge findOne(Long badge_id) {
        return badgeRepository.findOne(badge_id);
    }
    
    
    /**
     * 현재 저장된 모든 뱃지를 가져온다.
     * @return
     */
    public List<Badge> findAllBadge() {
        return badgeRepository.findAll();
    }

    

    /**
     * 새로운 뱃지를 생성한다.
     * (관리자의 권한이 필요하다)
     * @param name
     * @param image_url
     * @return
     */
    @Transactional
    public Long create(String name, String image_url) {
        Badge badge = new Badge();
        badge.setName(name);
        badge.setImage_url(image_url);
        badgeRepository.save(badge);
        return badge.getId();
    }

    /**
     * 뱃지 정보를 수정한다.
     * @param badge_id
     * @param badgeDto
     */
    @Transactional
    public void update(Long badge_id, UpdateBadgeDto badgeDto) {
        Badge badge = badgeRepository.findOne(badge_id);
        badge.setName(badgeDto.getName());
        badge.setImage_url(badgeDto.getImage_url());
    }

    /**
     * 저장된 뱃지를 삭제한다.
     * @param badge_id
     */
    @Transactional
    public void delete(Long badge_id) {
        badgeRepository.deleteById(badge_id);
    }
    
}
