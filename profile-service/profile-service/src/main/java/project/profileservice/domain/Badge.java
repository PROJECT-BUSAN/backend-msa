package project.profileservice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Badge {

    @Id @GeneratedValue
    @Column(name = "badge_id")
    private Long id;
    private String name;
    private String image_url;

    @OneToMany
    @JoinColumn(name = "badge_id")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();

}
