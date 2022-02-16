package project.profileservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue
    @Column(name = "profile_id")
    private Long id;
    private Long user_id;
    private int strick;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();

    /**
     * 연관관계 메서드
     */
    // DI Profile -> ProfileBadge
    public void InsertProfileBadge(ProfileBadge profileBadge) {
        this.profileBadges.add(profileBadge);
        profileBadge.setProfile(this);
    }

    /**
     * profileBadges 추가 메서드
     */

    // Profile에 ProfileBadge 추가
    public void AddProfileBadge(ProfileBadge... profileBadges) {
        for (ProfileBadge profileBadge : profileBadges) {
            this.InsertProfileBadge(profileBadge);
        }
    }
}
