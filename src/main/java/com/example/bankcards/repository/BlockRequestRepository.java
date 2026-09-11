package com.example.bankcards.repository;

import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockRequestRepository extends JpaRepository<BlockRequest, Long> {
    boolean existsByCardAndStatus(Card card, BlockRequestStatus status);
    List<BlockRequest> findAllByStatus(BlockRequestStatus status);
    Optional<BlockRequest> findByIdAndStatus(Long id, BlockRequestStatus status);
}
