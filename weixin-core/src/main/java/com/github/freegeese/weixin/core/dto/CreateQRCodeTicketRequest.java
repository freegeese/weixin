package com.github.freegeese.weixin.core.dto;

import lombok.Data;

public class CreateQRCodeTicketRequest {
    private String access_token;
    private Integer expire_seconds;
    private String action_name;
    private ActionInfo action_info;

    private CreateQRCodeTicketRequest() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(Integer expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getAction_name() {
        return action_name;
    }

    public ActionInfo getAction_info() {
        return action_info;
    }

    public CreateQRCodeTicketRequest setSceneId(Integer sceneId) {
        getAction_info().getScene().setScene_id(sceneId);
        return this;
    }

    public CreateQRCodeTicketRequest setSceneStr(String sceneStr) {
        getAction_info().getScene().setScene_str(sceneStr);
        return this;
    }

    @Data
    private static class ActionInfo {
        private Scene scene;
    }

    @Data
    private static class Scene {
        private Integer scene_id;
        private String scene_str;
    }

    public static CreateQRCodeTicketRequest newInstance(boolean limit, boolean str) {
        StringBuilder qrCodeType = new StringBuilder("QR_");
        if (limit) {
            qrCodeType.append("LIMIT_");
        }
        if (str) {
            qrCodeType.append("STR_");
        }
        qrCodeType.append("SCENE");
        return newInstance(qrCodeType.toString());
    }

    /**
     * 二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
     *
     * @param actionName
     * @return
     */
    public static CreateQRCodeTicketRequest newInstance(String actionName) {
        CreateQRCodeTicketRequest request = new CreateQRCodeTicketRequest();
        Scene scene = new Scene();
        ActionInfo actionInfo = new ActionInfo();
        actionInfo.setScene(scene);
        request.action_info = actionInfo;
        return request;
    }
}
