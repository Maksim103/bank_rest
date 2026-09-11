package com.example.bankcards.controller;

import com.example.bankcards.dto.BlockRequestResponseDTO;
import com.example.bankcards.service.AdminBlockRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/block-requests")
public class AdminBlockRequestController {

    private final AdminBlockRequestService adminBlockRequestService;

    public AdminBlockRequestController(AdminBlockRequestService adminBlockRequestService) {
        this.adminBlockRequestService = adminBlockRequestService;
    }

    @GetMapping
    public ResponseEntity<List<BlockRequestResponseDTO>> getPendingBlockRequests() {
        List<BlockRequestResponseDTO> allBlockRequests = adminBlockRequestService.getPendingBlockRequests();
        return ResponseEntity.ok(allBlockRequests);
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<BlockRequestResponseDTO> approveBlockRequest(@PathVariable Long requestId) {
        BlockRequestResponseDTO approvedRequest = adminBlockRequestService.approveBlockRequest(requestId);
        return ResponseEntity.ok(approvedRequest);
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<BlockRequestResponseDTO> rejectBlockRequest(@PathVariable Long requestId) {
        BlockRequestResponseDTO rejectedRequest = adminBlockRequestService.rejectBlockRequest(requestId);
        return ResponseEntity.ok(rejectedRequest);
    }
}
