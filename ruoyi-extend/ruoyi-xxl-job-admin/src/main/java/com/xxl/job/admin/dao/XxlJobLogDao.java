package com.xxl.job.admin.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.admin.core.model.XxlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;
import java.util.stream.Collectors;

/**
 * job log
 *
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao extends BaseMapper<XxlJobLog> {

    // exist jobId not use jobGroup, not exist use jobGroup
    default Page<XxlJobLog> pageList(@Param("offset") int offset,
                                    @Param("pagesize") int pagesize,
                                    @Param("jobGroup") int jobGroup,
                                    @Param("jobId") int jobId,
                                    @Param("triggerTimeStart") Date triggerTimeStart,
                                    @Param("triggerTimeEnd") Date triggerTimeEnd,
                                    @Param("logStatus") int logStatus) {
        return this.selectPage(new Page<>(offset, pagesize),
            new LambdaQueryWrapper<XxlJobLog>()
                .eq(jobId == 0 && jobGroup > 0, XxlJobLog::getJobGroup, jobGroup)
                .eq(jobId > 0, XxlJobLog::getJobId, jobId)
                .ge(triggerTimeStart != null, XxlJobLog::getTriggerTime, triggerTimeStart)
                .le(triggerTimeEnd != null, XxlJobLog::getTriggerTime, triggerTimeEnd)
                .eq(logStatus == 1, XxlJobLog::getHandleCode, 200)
                .and(logStatus == 2, lqw -> {
                    lqw.notIn(XxlJobLog::getTriggerCode, 0, 200)
                        .or()
                        .notIn(XxlJobLog::getHandleCode, 0, 200);
                })
                .eq(logStatus == 3, XxlJobLog::getTriggerCode, 200)
                .eq(logStatus == 3, XxlJobLog::getHandleCode, 0)
                .orderByDesc(XxlJobLog::getTriggerTime));
    }

//    public int pageListCount(@Param("offset") int offset,
//                             @Param("pagesize") int pagesize,
//                             @Param("jobGroup") int jobGroup,
//                             @Param("jobId") int jobId,
//                             @Param("triggerTimeStart") Date triggerTimeStart,
//                             @Param("triggerTimeEnd") Date triggerTimeEnd,
//                             @Param("logStatus") int logStatus);

    default XxlJobLog load(@Param("id") long id) {
        return this.selectById(id);
    }

    default long save(XxlJobLog xxlJobLog) {
        return this.insert(xxlJobLog);
    }

    default int updateTriggerInfo(XxlJobLog xxlJobLog) {
        return this.update(null, new LambdaUpdateWrapper<XxlJobLog>()
            .set(XxlJobLog::getTriggerTime, xxlJobLog.getTriggerTime())
            .set(XxlJobLog::getTriggerCode, xxlJobLog.getTriggerCode())
            .set(XxlJobLog::getTriggerMsg, xxlJobLog.getTriggerMsg())
            .set(XxlJobLog::getExecutorAddress, xxlJobLog.getExecutorAddress())
            .set(XxlJobLog::getExecutorHandler, xxlJobLog.getExecutorHandler())
            .set(XxlJobLog::getExecutorParam, xxlJobLog.getExecutorParam())
            .set(XxlJobLog::getExecutorShardingParam, xxlJobLog.getExecutorShardingParam())
            .set(XxlJobLog::getExecutorFailRetryCount, xxlJobLog.getExecutorFailRetryCount())
            .eq(XxlJobLog::getId, xxlJobLog.getId()));
    }

    default int updateHandleInfo(XxlJobLog xxlJobLog) {
        return this.update(null, new LambdaUpdateWrapper<XxlJobLog>()
            .set(XxlJobLog::getHandleTime, xxlJobLog.getHandleTime())
            .set(XxlJobLog::getHandleCode, xxlJobLog.getHandleCode())
            .set(XxlJobLog::getHandleMsg, xxlJobLog.getHandleMsg())
            .eq(XxlJobLog::getId, xxlJobLog.getId()));
    }

    default int delete(@Param("jobId") int jobId) {
        return this.delete(new LambdaQueryWrapper<XxlJobLog>().eq(XxlJobLog::getJobId, jobId));
    }

    default Map<String, Object> findLogReport(@Param("from") Date from,
                                             @Param("to") Date to) {
        Map<String, Object> map = new HashMap<>(3);
        List<XxlJobLog> list = this.selectList(new LambdaQueryWrapper<XxlJobLog>().between(XxlJobLog::getTriggerTime, from, to));
        if (CollUtil.isEmpty(list)) {
            return map;
        }
        int triggerDayCountRunning = list.stream().mapToInt(x -> {
            if (x.getTriggerCode() == 0 || x.getTriggerCode() == 200) {
                return x.getHandleCode() == 0 ? 1 : 0;
            } else {
                return 0;
            }
        }).sum();
        int triggerDayCountSuc = list.stream().mapToInt(x -> x.getHandleCode() == 200 ? 1 : 0).sum();
        map.put("triggerDayCount", list.size());
        map.put("triggerDayCountRunning", triggerDayCountRunning);
        map.put("triggerDayCountSuc", triggerDayCountSuc);
        return map;
    }

    default List<Long> findClearLogIds(@Param("jobGroup") int jobGroup,
                                      @Param("jobId") int jobId,
                                      @Param("clearBeforeTime") Date clearBeforeTime,
                                      @Param("clearBeforeNum") int clearBeforeNum,
                                      @Param("pagesize") int pagesize) {
        Page<XxlJobLog> page = this.selectPage(new Page<XxlJobLog>().setSize(pagesize),
            new LambdaQueryWrapper<XxlJobLog>()
                .eq(jobGroup > 0, XxlJobLog::getJobGroup, jobGroup)
                .eq(jobId > 0, XxlJobLog::getJobId, jobId)
                .le(clearBeforeTime != null, XxlJobLog::getTriggerTime, clearBeforeTime)
                .and(clearBeforeNum > 0, lqw -> {
                    Page<XxlJobLog> idsPage = this.selectPage(new Page<>(0, clearBeforeNum),
                        new LambdaQueryWrapper<XxlJobLog>()
                            .eq(jobGroup > 0, XxlJobLog::getJobGroup, jobGroup)
                            .eq(jobId > 0, XxlJobLog::getJobId, jobId)
                            .orderByDesc(XxlJobLog::getTriggerTime));
                    if (CollUtil.isEmpty(idsPage.getRecords())) {
                        lqw.notIn(XxlJobLog::getId, "");
                    } else {
                        lqw.notIn(XxlJobLog::getId, idsPage.getRecords().stream().map(XxlJobLog::getId).collect(Collectors.toList()));
                    }
                })
        );
        return CollUtil.isEmpty(page.getRecords()) ? new ArrayList<>() :
            page.getRecords().stream().map(XxlJobLog::getId).collect(Collectors.toList());
    }

    default int clearLog(@Param("logIds") List<Long> logIds) {
        return this.deleteBatchIds(logIds);
    }

    default List<Long> findFailJobLogIds(@Param("pagesize") int pagesize) {
        Page<XxlJobLog> page = this.selectPage(new Page<XxlJobLog>().setSize(pagesize),
            new LambdaQueryWrapper<XxlJobLog>()
                .select(XxlJobLog::getId)
                .and(lqw -> {
                    // (...) or (...)
                    lqw.nested(n -> n.in(XxlJobLog::getTriggerCode, 0, 200).eq(XxlJobLog::getHandleCode, 0))
                        .or()
                        .nested(n -> n.eq(XxlJobLog::getHandleCode, 200));
                })
                .eq(XxlJobLog::getAlarmStatus, 0)
                .orderByAsc(XxlJobLog::getId)
        );
        return CollUtil.isEmpty(page.getRecords()) ? new ArrayList<>() :
            page.getRecords().stream().map(XxlJobLog::getId).collect(Collectors.toList());
    }

    default int updateAlarmStatus(@Param("logId") long logId,
                                 @Param("oldAlarmStatus") int oldAlarmStatus,
                                 @Param("newAlarmStatus") int newAlarmStatus) {
        return this.update(null,
            new LambdaUpdateWrapper<XxlJobLog>()
                .set(XxlJobLog::getAlarmStatus, newAlarmStatus)
                .eq(XxlJobLog::getId, logId)
                .eq(XxlJobLog::getAlarmStatus, oldAlarmStatus));
    }

    public List<Long> findLostJobIds(@Param("losedTime") Date losedTime);

}
