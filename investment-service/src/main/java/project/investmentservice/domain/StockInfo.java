package project.investmentservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "stockinfo")
public class StockInfo {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private LocalDate date;
    private double close;
    private double open;
    private double high;
    private double low;
    private int volume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    
}

