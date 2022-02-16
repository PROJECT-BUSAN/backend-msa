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
    private int nowStrick;
    private int maxStrick;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private List<ProfileBadge> profileBadges = new ArrayList<ProfileBadge>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profile")
    private List<Attendance> attendances = new ArrayList<Attendance>();
    
    
    /**
     * profileBadges 추가 메서드
     */
    public void addProfileBadge(ProfileBadge profileBadge) {
        this.profileBadges.add(profileBadge);
    }

    /**
     * Attendance 추가 메서드
     */
    public void addAttendance(Attendance attendance) {
        this.attendances.add(attendance);
    }

    /**
     * 연속출석일수를 1로 리셋한다.
     */
    public void nowStrickReset() {
        this.nowStrick = 1;
    }

    /**
     * 연속출석일수를 1 증가시킨다.
     */
    public void nowStrickUp() {
        this.nowStrick += 1;
    }

    /**
     * 최대 연속출석일수를 갱신한다.
     */
    public void maxStrickUpdate() {
        this.maxStrick = Math.max(this.maxStrick, this.nowStrick);
    }
}
