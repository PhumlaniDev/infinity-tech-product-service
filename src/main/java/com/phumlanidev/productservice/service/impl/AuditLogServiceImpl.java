package com.phumlanidev.productservice.service.impl;


import com.phumlanidev.productservice.dto.AuditLogDto;
import com.phumlanidev.productservice.model.AuditLog;
import com.phumlanidev.productservice.repository.AuditLogRepository;
import com.phumlanidev.productservice.utils.AuditLogSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl {

    private final AuditLogRepository auditLogRepository;

    public void log(String action, String userId, String username, String ipAddress, String details) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .userId(userId)
                .username(username)
                .ipAddress(ipAddress)
                .details(details)
                .timestamp(Instant.now())
                .build();

        auditLogRepository.save(log);
    }

    public Page<AuditLogDto> getAuditLogs(String userId, String action, Pageable pageable) {
        Specification<AuditLog> spec = Specification.where(
                        AuditLogSpecifications.hasUserId(userId))
                .and(AuditLogSpecifications.hasAction(action));

        return auditLogRepository.findAll(spec, pageable).map(this::toDto);
    }

    private AuditLogDto toDto(AuditLog log) {
        return AuditLogDto.builder()
                .id(String.valueOf(log.getId()))
                .userId(log.getUserId())
                .username(log.getUsername())
                .action(log.getAction())
                .ip(log.getIpAddress())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}
