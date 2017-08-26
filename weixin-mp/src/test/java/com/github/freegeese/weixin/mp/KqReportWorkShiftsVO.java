package com.github.freegeese.weixin.mp;

import lombok.Data;

@Data
public class KqReportWorkShiftsVO {
    private Long projId;
    private String name;    //姓名
    private String workerNo; // 工号
    private String job; //工作
    private String reportDate; //日期
    private String workDuration;//工作时长
    private String lateTimes;  //迟到次数
    private String lateDuration;//迟到时长
    private String earlyTimes;//早退次数
    private String earlyDuration;//早退时长
    private String absentTimes;//缺卡次数
    private String absentDays;//缺卡类型
    private String checkInTime = "";
    private String checkOutTime = "";
    private String checkInStatus = "";
    private String checkOutStatus = "";//
    private Long reportId;//
    private Long num;//
    private Integer count = 0;// 计算表格下标
    private Long groupId;        // 班组id
    private String synthesize = "";//综合:查询 姓名/工号/手机号
    private String checkStatus = "";//考勤状态
}
