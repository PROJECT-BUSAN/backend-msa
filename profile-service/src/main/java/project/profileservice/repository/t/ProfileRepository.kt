package project.profileservice.repository.t

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.profileservice.domain.Profile

@Repository
interface ProfileRepository : JpaRepository<Profile, Long> {
}