package project.profileservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Badge {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long id;
    private String name;
    private String image_url;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "badge")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();

    
    /**
     * profileBadges 추가 메서드
     */
    public void addProfileBadge(ProfileBadge profileBadge) {
        this.profileBadges.add(profileBadge);
    }
}
