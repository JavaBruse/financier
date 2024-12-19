package org.MIFI.entity;

import lombok.Data;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Collection<Category> categories;
}
