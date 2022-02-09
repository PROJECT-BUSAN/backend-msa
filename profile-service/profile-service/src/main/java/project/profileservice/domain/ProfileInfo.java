package project.profileservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class ProfileInfo {

    @Id @GeneratedValue
    @Column(name = "profile_id")
    private Long id;
    private Long user_id;
    private int strick;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profileInfo")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();


    /**
     * 연관관계 메서드
     */
    public void addProfileBadge(ProfileBadge profileBadge) {
        this.profileBadges.add(profileBadge);
        profileBadge.setProfileInfo(this);
    }

    /**
     * profileBadges 추가 메서드
     */
    public void createProfileInfo(ProfileBadge... profileBadges) {
        for (ProfileBadge profileBadge : profileBadges) {
            this.addProfileBadge(profileBadge);
        }
    }
}