package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.UaBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UaBlacklistRepository extends JpaRepository<UaBlacklist, String> {
}
