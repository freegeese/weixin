package com.github.freegeese.weixin.mp.repository;

import com.github.freegeese.weixin.mp.domain.WxPayUnifiedOrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WxPayUnifiedOrderRequestRepository extends JpaRepository<WxPayUnifiedOrderRequest, Long> {
}
