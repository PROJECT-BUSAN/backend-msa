package project.profileservice.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import project.profileservice.domain.Badge
import project.profileservice.domain.Profile
import project.profileservice.domain.ProfileBadge
import project.profileservice.repository.t.BadgeRepository
import project.profileservice.repository.t.ProfileBadgeRepository
import project.profileservice.repository.t.ProfileRepository
import javax.transaction.Transactional

@Service
@RequiredArgsConstructor
class ProfileBadgeService
    (
    private val profileRepository : ProfileRepository,
    private val badgeRepository: BadgeRepository,
    private val profileBadgeRepository: ProfileBadgeRepository,
)

{
    @Transactional
    fun createProfileBadge(user_id : Long, badge_id : Long) {

        val profile : Profile = profileRepository.findById(user_id).orElseThrow()
        val badge : Badge = badgeRepository.findById(badge_id).orElseThrow()

        val profileBadge = ProfileBadge(profile, badge)
        profileBadgeRepository.save(profileBadge)
    }


}