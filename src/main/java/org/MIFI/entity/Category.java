package org.MIFI.entity;

import jakarta.persistence.*;
import java.util.*;

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

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Transaction> transactions;

    @Override
    public String toString() {
        return "    Категоря: " + name + getLimitText() + "\n";
    }

    private String getLimitText() {
        return this.limit > 0 ? ", лимит: " + limit : "";
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Category category = (Category) object;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }


    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }
}
