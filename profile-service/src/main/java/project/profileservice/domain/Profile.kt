package project.profileservice.domain

import lombok.Getter
import javax.persistence.*
import kotlin.math.max

@Entity
@Getter
class Profile
    (
    val user_id: Long,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    val id: Long? = null,
    var nowStrick: Int = 1,
    var maxStrick: Int = 1,
    var point: Double = 1000000.0,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "profile")
    val profileBadges: MutableList<ProfileBadge> = mutableListOf(),
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "profile")
    var attendances: MutableList<Attendance> = mutableListOf(),

    ) {
    fun addAttendance(attendance: Attendance) {
        this.attendances.add(attendance)
    }

    fun updatePoint(point: Double): Double {
        val currentPoint: Double = this.point + point
        if (isPointLessThan0(currentPoint)) {
            // Exception
        }
        this.point = currentPoint
        return currentPoint
    }

    fun addProfileBadge(profileBadge: ProfileBadge) {
        this.profileBadges.add(profileBadge)
    }

    fun addNowStrick() {
        this.nowStrick++
    }

    fun calMaxStrick() {
        this.maxStrick = max(this.nowStrick, maxStrick)
    }

    fun nowStrickReset() {
        this.nowStrick = 0
    }

    private fun isPointLessThan0(point: Double): Boolean {
        return point < 0
    }
}