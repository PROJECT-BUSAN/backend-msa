package project.profileservice.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.profileservice.domain.Attendance
import project.profileservice.domain.Profile
import project.profileservice.repository.t.AttendanceRepository
import project.profileservice.repository.t.ProfileRepository
import java.time.LocalDate
import java.time.Period

@Service
@RequiredArgsConstructor
class AttendanceServiceImpl
  (
    private val attendanceRepository: AttendanceRepository,
    private val profileRepository: ProfileRepository
) {

    @Transactional(readOnly = true)
    fun findAllByUserId(id : Long) : List<Attendance> {
        val profile : Profile = profileRepository.findById(id).orElseThrow()
        return attendanceRepository.findAllByProfile(profile)
    }


    @Transactional
    fun attendByUserId(id : Long) {
        val profile : Profile = profileRepository.findById(id).orElseThrow()
        if (isValidAttend(profile)) {
            updateStrick(profile)
            val attendance = Attendance(profile)
            attendanceRepository.save(attendance)
        }
    }

    fun updateStrick(profile: Profile) {
        val attendance : Attendance = profile.attendances[0]
        val period = attendance.getPeriod()
        if(isDifferenceOneDay(period)) {
            profile.addNowStrick()
            profile.calMaxStrick()
        }
        if(isDifferenceBiggerThanOneDay(period)) {
            profile.nowStrickReset()
        }
    }

    private fun isValidAttend(profile: Profile) : Boolean {
        val attendance : Attendance = profile.attendances[0]
        return attendance.createAt.toLocalDate() == LocalDate.now()
    }

    private fun isDifferenceOneDay(period: Period) : Boolean {
      return period.days == 1
    }

    private fun isDifferenceBiggerThanOneDay(period: Period) : Boolean {
      return period.days >= 2
    }
}