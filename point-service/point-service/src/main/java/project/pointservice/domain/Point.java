package project.pointservice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Point {

    @Id @GeneratedValue
    @Column(name = "point_id")
    private Long id;
    private Long user_id;
    private double currentPoint;

    @OneToMany
    @JoinColumn(name = "point_id")
    private List<PointLog> pointLogs = new ArrayList<PointLog>();

}