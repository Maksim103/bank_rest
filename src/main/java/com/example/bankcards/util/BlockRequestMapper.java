package com.example.bankcards.util;

import com.example.bankcards.dto.BlockRequestResponseDTO;
import com.example.bankcards.entity.BlockRequest;
import org.springframework.stereotype.Component;

@Component
public class BlockRequestMapper {
    public BlockRequestResponseDTO toBlockRequestResponseDTO(BlockRequest blockRequest) {
        if (blockRequest == null) {
            return null;
        }

        BlockRequestResponseDTO responseDTO = new BlockRequestResponseDTO();

        responseDTO.setId(blockRequest.getId());
        responseDTO.setCardId(blockRequest.getCard().getId());
        responseDTO.setRequesterId(blockRequest.getRequester().getId());
        responseDTO.setStatus(blockRequest.getStatus());

        return responseDTO;
    }
}
