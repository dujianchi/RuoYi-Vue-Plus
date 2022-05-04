package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * xxl-job log, used to track trigger process
 *
 * @author xuxueli  2015-12-19 23:19:09
 */
@Data
public class XxlJobLog {

    @TableId
    private Long id;

    // job info
    private Integer jobGroup = 0;
    private Integer jobId = 0;

    // execute info
    private String executorAddress;
    private String executorHandler;
    private String executorParam;
    private String executorShardingParam;
    private Integer executorFailRetryCount = 0;

    // trigger info
    private Date triggerTime;
    private Integer triggerCode = 0;
    private String triggerMsg;

    // handle info
    private Date handleTime;
    private Integer handleCode = 0;
    private String handleMsg;

    // alarm info
    private Integer alarmStatus = 0;

}
