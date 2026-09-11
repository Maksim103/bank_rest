package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.BlockRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlockRequestResponseDTO {
    private Long id;
    private Long cardId;
    private Long requesterId;
    private BlockRequestStatus status;
}
