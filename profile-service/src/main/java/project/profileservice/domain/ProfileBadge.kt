package project.profileservice.domain

import lombok.Getter
import javax.persistence.*

@Entity
@Getter
class ProfileBadge(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    val profile: Profile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    val badge: Badge,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "profile_badge_id")
    var id: Long? = null

    )
{
    init {
        profile.addProfileBadge(this)
        badge.addProfileBadge(this)
    }
}