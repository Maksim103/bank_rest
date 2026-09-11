package com.example.bankcards.entity;

import com.example.bankcards.entity.enums.BlockRequestStatus;
import jakarta.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"card", "requester"})
@Getter
@Setter
@Entity
@Table(name = "block_requests")
public class BlockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BlockRequestStatus status;

    public BlockRequest() {

    }

    public BlockRequest(Card card, User requester, BlockRequestStatus status) {
        this.card = card;
        this.requester = requester;
        this.status = status;
    }
}
