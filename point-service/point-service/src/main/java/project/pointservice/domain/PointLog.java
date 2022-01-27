package project.pointservice.domain;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class PointLog {

    @Id @GeneratedValue
    @Column(name = "log_id")
    private Long id;
    private Date createdAt;
    private String source;
    private double positive;
    private double negative;

}
