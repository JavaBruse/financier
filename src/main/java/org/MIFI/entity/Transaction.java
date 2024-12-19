package org.MIFI.entity;

import org.MIFI.entity.enums.TypeOfTransaction;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeOfTransaction type;

    @Column(name = "created")
    private Long created;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
