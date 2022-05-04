package com.xxl.job.admin.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.admin.core.model.XxlJobLogGlue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.stream.Collectors;

/**
 * job log for glue
 *
 * @author xuxueli 2016-5-19 18:04:56
 */
@Mapper
public interface XxlJobLogGlueDao extends BaseMapper<XxlJobLogGlue> {

    default int save(XxlJobLogGlue xxlJobLogGlue) {
        return this.insert(xxlJobLogGlue);
    }

    default List<XxlJobLogGlue> findByJobId(@Param("jobId") int jobId) {
        return this.selectList(new LambdaQueryWrapper<XxlJobLogGlue>()
            .eq(XxlJobLogGlue::getJobId, jobId)
            .orderByDesc(XxlJobLogGlue::getId));
    }

    default int removeOld(@Param("jobId") int jobId, @Param("limit") int limit) {
        Page<XxlJobLogGlue> page = this.selectPage(new Page<XxlJobLogGlue>().setSize(limit),
            new LambdaQueryWrapper<XxlJobLogGlue>().select(XxlJobLogGlue::getId)
                .eq(XxlJobLogGlue::getJobId, jobId)
                .orderByDesc(XxlJobLogGlue::getUpdateTime));
        List<Integer> ids = page.getRecords().stream().map(XxlJobLogGlue::getId).collect(Collectors.toList());
        return this.delete(
            new LambdaQueryWrapper<XxlJobLogGlue>()
                .notIn(XxlJobLogGlue::getId, ids)
                .eq(XxlJobLogGlue::getJobId, jobId));
    }

    default int deleteByJobId(@Param("jobId") int jobId) {
        return this.delete(new LambdaQueryWrapper<XxlJobLogGlue>().eq(XxlJobLogGlue::getJobId, jobId));
    }

}
