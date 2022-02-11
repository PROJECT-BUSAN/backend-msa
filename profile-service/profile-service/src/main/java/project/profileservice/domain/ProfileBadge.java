package project.profileservice.domain;
import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class ProfileBadge {
    @Id @GeneratedValue
    @Column(name = "profile_badge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;


    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
        badge.getProfileBadges().add(this);
    }

    /**
     * 생성 메서드
     */
    public static ProfileBadge CreateProfileBadge(Badge badge) {
        ProfileBadge profileBadge = new ProfileBadge();
        profileBadge.setBadge(badge);
        return profileBadge;
    }
}
