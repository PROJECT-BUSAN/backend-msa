package project.profileservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Badge {

    @Id @GeneratedValue
    @Column(name = "badge_id")
    private Long id;
    private String name;
    private String image_url;

    @OneToMany(mappedBy = "badge")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();

}
