package com.xxl.job.admin.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.admin.core.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * job info
 *
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoDao extends BaseMapper<XxlJobInfo> {

    default Page<XxlJobInfo> pageList(@Param("offset") int offset,
                                      @Param("pagesize") int pagesize,
                                      @Param("jobGroup") int jobGroup,
                                      @Param("triggerStatus") int triggerStatus,
                                      @Param("jobDesc") String jobDesc,
                                      @Param("executorHandler") String executorHandler,
                                      @Param("author") String author) {
        return this.selectPage(new Page<>(offset, pagesize),
            new LambdaQueryWrapper<XxlJobInfo>()
                .eq(jobGroup > 0, XxlJobInfo::getJobGroup, jobGroup)
                .eq(triggerStatus >= 0, XxlJobInfo::getTriggerStatus, triggerStatus)
                .like(StrUtil.isNotBlank(jobDesc), XxlJobInfo::getJobDesc, jobDesc)
                .like(StrUtil.isNotBlank(executorHandler), XxlJobInfo::getExecutorHandler, executorHandler)
                .like(StrUtil.isNotBlank(author), XxlJobInfo::getAuthor, author)
                .orderByDesc(XxlJobInfo::getId));
    }

    default long pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("jobGroup") int jobGroup,
                             @Param("triggerStatus") int triggerStatus,
                             @Param("jobDesc") String jobDesc,
                             @Param("executorHandler") String executorHandler,
                             @Param("author") String author) {
        return this.selectCount(
            new LambdaQueryWrapper<XxlJobInfo>()
                .eq(jobGroup > 0, XxlJobInfo::getJobGroup, jobGroup)
                .eq(triggerStatus >= 0, XxlJobInfo::getTriggerStatus, triggerStatus)
                .like(StrUtil.isNotBlank(jobDesc), XxlJobInfo::getJobDesc, jobDesc)
                .like(StrUtil.isNotBlank(executorHandler), XxlJobInfo::getExecutorHandler, executorHandler)
                .like(StrUtil.isNotBlank(author), XxlJobInfo::getAuthor, author));
    }

    default int save(XxlJobInfo info) {
        return this.insert(info);
    }

    default XxlJobInfo loadById(@Param("id") int id) {
        return this.selectById(id);
    }

    default int update(XxlJobInfo xxlJobInfo) {
        return this.updateById(xxlJobInfo);
    }

    default int delete(@Param("id") long id) {
        return this.deleteById(id);
    }

    default List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup) {
        return this.selectList(new LambdaQueryWrapper<XxlJobInfo>().eq(XxlJobInfo::getJobGroup, jobGroup));
    }

    default long findAllCount() {
        return this.selectCount(null);
    }

    default Page<XxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize) {
        return this.selectPage(new Page<XxlJobInfo>().setSize(pagesize),
            new LambdaQueryWrapper<XxlJobInfo>()
                .eq(XxlJobInfo::getTriggerStatus, 1)
                .le(XxlJobInfo::getTriggerNextTime, maxNextTime)
                .orderByAsc(XxlJobInfo::getId));
    }

    default int scheduleUpdate(XxlJobInfo xxlJobInfo) {
        return this.update(null, new LambdaUpdateWrapper<XxlJobInfo>()
            .set(XxlJobInfo::getTriggerLastTime, xxlJobInfo.getTriggerLastTime())
            .set(XxlJobInfo::getTriggerNextTime, xxlJobInfo.getTriggerNextTime())
            .set(XxlJobInfo::getTriggerStatus, xxlJobInfo.getTriggerStatus())
            .eq(XxlJobInfo::getId, xxlJobInfo.getId()));
    }


}
