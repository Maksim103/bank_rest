package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"password", "cards"})
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role", unique = true)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner",
                fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    private List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        if (!cards.contains(card)){
            card.setOwner(this);
            cards.add(card);
        }
    }
}
