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

    @OneToMany
    @JoinColumn(name = "profile_id")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();

}
