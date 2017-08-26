package com.github.freegeese.weixin.mp.repository;

import com.github.freegeese.weixin.mp.domain.WxPayUnifiedOrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WxPayUnifiedOrderResponseRepository extends JpaRepository<WxPayUnifiedOrderResponse, Long> {
}
