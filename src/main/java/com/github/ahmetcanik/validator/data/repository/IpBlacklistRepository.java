package com.github.ahmetcanik.validator.data.repository;

import com.github.ahmetcanik.validator.data.entity.IpBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpBlacklistRepository extends JpaRepository<IpBlacklist, Long> {
}
