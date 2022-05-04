package com.xxl.job.admin.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.XxlJobLogReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * job log
 *
 * @author xuxueli 2019-11-22
 */
@Mapper
public interface XxlJobLogReportDao extends BaseMapper<XxlJobLogReport> {

    default int save(XxlJobLogReport xxlJobLogReport) {
        return this.insert(xxlJobLogReport);
    }

    default int update(XxlJobLogReport xxlJobLogReport) {
        return this.update(null,
            new LambdaUpdateWrapper<XxlJobLogReport>()
                .set(XxlJobLogReport::getRunningCount, xxlJobLogReport.getRunningCount())
                .set(XxlJobLogReport::getSucCount, xxlJobLogReport.getSucCount())
                .set(XxlJobLogReport::getFailCount, xxlJobLogReport.getFailCount())
                .eq(XxlJobLogReport::getTriggerDay, xxlJobLogReport.getTriggerDay()));
    }

    default List<XxlJobLogReport> queryLogReport(@Param("triggerDayFrom") Date triggerDayFrom,
                                                @Param("triggerDayTo") Date triggerDayTo) {
        return this.selectList(
            new LambdaQueryWrapper<XxlJobLogReport>()
                .between(XxlJobLogReport::getTriggerDay, triggerDayFrom, triggerDayTo)
                .orderByAsc(XxlJobLogReport::getTriggerDay));
    }

    default XxlJobLogReport queryLogReportTotal() {
        List<XxlJobLogReport> list = this.selectList(null);
        int runningCount = list.stream().mapToInt(XxlJobLogReport::getRunningCount).sum();
        int sucCount = list.stream().mapToInt(XxlJobLogReport::getSucCount).sum();
        int failCount = list.stream().mapToInt(XxlJobLogReport::getFailCount).sum();
        XxlJobLogReport xxlJobLogReport = new XxlJobLogReport();
        xxlJobLogReport.setRunningCount(runningCount);
        xxlJobLogReport.setSucCount(sucCount);
        xxlJobLogReport.setFailCount(failCount);
        return xxlJobLogReport;
    }

}
