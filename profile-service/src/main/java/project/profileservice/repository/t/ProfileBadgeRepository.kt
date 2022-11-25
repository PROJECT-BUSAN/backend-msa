package project.profileservice.repository.t

import org.springframework.data.jpa.repository.JpaRepository
import project.profileservice.domain.ProfileBadge

interface ProfileBadgeRepository : JpaRepository<ProfileBadge, Long> {
}