package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class XxlJobLogReport {

    @TableId
    private Integer id;

    private Date triggerDay;

    private Integer runningCount = 0;
    private Integer sucCount = 0;
    private Integer failCount = 0;

}
