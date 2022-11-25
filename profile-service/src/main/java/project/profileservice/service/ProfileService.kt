package project.profileservice.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.profileservice.domain.Attendance
import project.profileservice.domain.Profile
import project.profileservice.repository.t.AttendanceRepository
import project.profileservice.repository.t.BadgeRepository
import project.profileservice.repository.t.ProfileBadgeRepository
import project.profileservice.repository.t.ProfileRepository

@Service
@RequiredArgsConstructor
class ProfileService (
    private val profileRepository : ProfileRepository,
    private val badgeRepository: BadgeRepository,
    private val profileBadgeRepository: ProfileBadgeRepository,
    private val attendanceRepository : AttendanceRepository
){


    /**
     * user_id를 통해 프로필의 '모든' 정보를 가져온다.
     * @param user_id
     * @return Profile
     */
    fun findOneById(id : Long) : Profile {
        return profileRepository.findById(id).orElseThrow()
    }

    /**
     * 모든 유저의 프로필을 가져온다.(관리자 페이지가 생긴다면 쓰일 듯하다)
     * @return
     */
    fun findAll() : List<Profile> {
        return profileRepository.findAll()
    }

    @Transactional
    fun create(id : Long) {
        val profile = Profile(id)
        val attendance = Attendance(profile)
        attendanceRepository.save(attendance)
        profileRepository.save(profile)
    }

    @Transactional
    fun delete(id : Long) {
        val profile : Profile = profileRepository.findById(id)
            .orElseThrow()
        profileRepository.delete(profile)
    }

    @Transactional
    fun updatePoint(id : Long, point : Double) : Double {
        val profile : Profile = profileRepository.findById(id)
            .orElseThrow()
        return profile.updatePoint(point)
    }
}