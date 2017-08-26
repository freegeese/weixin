package com.github.freegeese.weixin.pay.native0.dto;

import com.github.freegeese.weixin.pay.common.PayBill;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
@XStreamAlias("xml")
public class NativeDownloadBillResponse extends PayBill {
    private String return_code;
    private String return_msg;
}
