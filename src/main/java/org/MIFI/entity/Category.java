package org.MIFI.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.MIFI.exceptions.NotFoundMessageException;


import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@EqualsAndHashCode(of = "name")
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

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Transaction> transactions;

    @Override
    public String toString() {
        return "    Категоря: " + name +
                ", лимит: " + limit + "\n";
    }

    public Category getExpenses() {
        List<Transaction> l = getTransExp();
        Category category = new Category();
        category.setName(this.getName());
        category.setTransactions(l);
        return category;
    }

    public Category getIncome() {
        List<Transaction> l = getTransInc();
        Category category = new Category();
        category.setName(this.getName());
        category.setTransactions(l);
        return category;
    }

    private List<Transaction> getTransExp() {
        List<Transaction> list = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getExpenses() != null) {
                list.add(t.getExpenses());
            }
        }
        return list;
    }

    private List<Transaction> getTransInc() {
        List<Transaction> list = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getIncome() != null) {
                list.add(t.getIncome());
            }
        }
        return list;
    }

}
