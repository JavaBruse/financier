package org.MIFI.entity;

import org.MIFI.entity.enums.TypeOfTransaction;
import jakarta.persistence.*;
import java.text.SimpleDateFormat;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeOfTransaction type;

    @Column(name = "created")
    private Long created;

    @Column(name = "money")
    private Double money;

    @Override
    public String toString() {
        return "  Транзакция: " + description +
                ", Деньги: " + money +
                ", Дата: " + getDateString() + "\n";
    }

    public Transaction getExpenses() {
        if (this.type == TypeOfTransaction.OUT) {
            return this;
        } else {
            return null;
        }
    }

    public Transaction getIncome() {
        if (this.type == TypeOfTransaction.IN) {
            return this;
        } else {
            return null;
        }
    }

    public void setMoney(Double money) {
        this.money = money;
        if (money > 0) {
            this.type = TypeOfTransaction.IN;
        } else {
            this.type = TypeOfTransaction.OUT;
        }
    }

    public String getDateString() {
        return new SimpleDateFormat("dd.MM.yyг.").format(created);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TypeOfTransaction type) {
        this.type = type;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Double getMoney() {
        return money;
    }
}
