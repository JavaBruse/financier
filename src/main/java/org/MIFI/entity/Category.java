package org.MIFI.entity;


import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category")
    private Collection<Transaction> transactions;

    @Column(name = "name")
    private String name;

    @Column(name = "limit")
    private Double limit;
}
