package geektime.spring.data.mybatisdemo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import lombok.*;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "T_COFFEE")
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coffee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
