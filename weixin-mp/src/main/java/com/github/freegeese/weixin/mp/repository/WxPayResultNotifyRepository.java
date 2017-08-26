package com.github.freegeese.weixin.mp.repository;

import com.github.freegeese.weixin.mp.domain.WxPayResultNotify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WxPayResultNotifyRepository extends JpaRepository<WxPayResultNotify, Long> {
}
