package project.profileservice.repository.t

import org.springframework.data.jpa.repository.JpaRepository
import project.profileservice.domain.Attendance
import project.profileservice.domain.Profile

interface AttendanceRepository : JpaRepository<Attendance, Long> {
    fun findAllByProfile(profile: Profile) : List<Attendance>
}