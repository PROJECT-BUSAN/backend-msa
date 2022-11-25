package project.profileservice.domain

import lombok.Getter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import javax.persistence.*

@Entity
@Getter
class Attendance(

    val profile: Profile,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    val createAt: LocalDateTime = LocalDateTime.now(),
) {

    init {
        profile.addAttendance(this)
    }

    fun getPeriod() : Period {
        return Period.between(createAt.toLocalDate(), LocalDate.now())
    }
}