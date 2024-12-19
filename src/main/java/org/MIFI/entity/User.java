package org.MIFI.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collection;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Category> categories;

    @Override
    public String toString() {
        return "Пользователь: " + name + '\n' + printCategories();
    }

    private String printCategories() {
        StringBuilder sb = new StringBuilder();
        for (Category t : categories) {
            sb.append(t);
        }
        return sb.toString();
    }

}
