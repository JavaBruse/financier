package org.MIFI.entity;

import lombok.Data;
import org.MIFI.entity.enums.TypeOfTransaction;

import jakarta.persistence.*;

import java.util.Date;

@Data
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


    @Override
    public String toString() {
        return  "Транзакция: Описание: " + description + '\'' +
                ", Тип: " + getType(type) +
                ", Дата: " + new Date(created)+ "\n";
    }

    private String getType(TypeOfTransaction type){
        if (type == TypeOfTransaction.IN) {
            return "пополнение";
        }else{
            return "растрата";
        }

    }
}
