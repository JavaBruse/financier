package org.MIFI.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.util.Collection;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "max_limit")
    private Double limit;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;

    @Override
    public String toString() {
        return "Категоря: " + name +
                ", Лимит: " + limit + "\n" +printTransaction();
    }

    private String printTransaction(){
        StringBuilder sb = new StringBuilder();
        for (Transaction t:transactions) {
            sb.append(t);
        }
        return sb.toString();
    }

}
