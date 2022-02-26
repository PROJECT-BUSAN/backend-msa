package project.investmentservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "company")
public class Company {
    
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    
    private String stock_name;
    private String stock_code;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<StockInfo> profileBadges = new ArrayList<StockInfo>();
    
}
