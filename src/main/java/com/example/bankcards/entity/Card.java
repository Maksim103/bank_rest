package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"cardNumber"})
@SQLDelete(sql = "UPDATE cards SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "card_number")
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "card_status")
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
