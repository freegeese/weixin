package com.github.freegeese.weixin.mp;

import com.alibaba.fastjson.JSON;
import com.geese.plugin.excel.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WcmTests {

    @Test
    public void test() throws Exception {
        List<Map> data = new ArrayList<>();
        KqReportWorkShiftsVO vo = new KqReportWorkShiftsVO();
        vo.setName("zhangsan");
        data.add((Map) JSON.toJSON(vo));
        // 姓名	编号	职位	日期  工作时长(分钟)	迟到次数	迟到时长(分钟)	早退次数	早退时长(分钟)	缺卡次数	旷工类型	操作
        String[] columns = {"name", "workerNo", "job", "reportDate", "checkInTime", "checkInStatus", "checkOutTime", "checkOutStatus", "workDuration", "lateTimes", "lateDuration", "earlyTimes", "earlyDuration", "absentTimes", "absentDays", "action"};
        String insert = StringUtils.join(columns, ",");
        FileOutputStream output = new FileOutputStream(new File("D:/wcm.xls"));
        ExcelWriter.newInstance(output).insert(insert + " into 0 limit 1").addData(data).execute();
    }
}
