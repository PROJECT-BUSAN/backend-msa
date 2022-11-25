package project.profileservice.domain

import lombok.Getter
import project.profileservice.domain.dto.UpdateBadgeDto
import javax.persistence.*

@Entity
@Getter
class Badge
    (
    var name: String,
    var image_url: String,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "badge_id")
    var id: Long? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "badge")
    val profileBadges: MutableList<ProfileBadge> = mutableListOf(),

    ) {
    fun addProfileBadge(profileBadge: ProfileBadge) {
        this.profileBadges.add(profileBadge)
    }

    fun updateByDto(badgeDto: UpdateBadgeDto) {
        this.name = badgeDto.name
        this.image_url = badgeDto.image_url
    }
}