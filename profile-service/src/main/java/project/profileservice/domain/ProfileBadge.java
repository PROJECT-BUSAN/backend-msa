package project.profileservice.domain;
import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class ProfileBadge {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_badge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    /**
     * 생성 메서드
     */
    public void CreateProfileBadge(Profile profile, Badge badge) {
        this.badge = badge;
        this.profile = profile;
    }
}
