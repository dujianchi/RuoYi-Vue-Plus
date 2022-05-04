package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * Created by xuxueli on 16/9/30.
 */
@Data
public class XxlJobRegistry {

    @TableId
    private Integer id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private Date updateTime;

}
