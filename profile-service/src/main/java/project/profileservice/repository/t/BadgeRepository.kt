package project.profileservice.repository.t

import org.springframework.data.jpa.repository.JpaRepository
import project.profileservice.domain.Badge

interface BadgeRepository : JpaRepository<Badge, Long> {
}