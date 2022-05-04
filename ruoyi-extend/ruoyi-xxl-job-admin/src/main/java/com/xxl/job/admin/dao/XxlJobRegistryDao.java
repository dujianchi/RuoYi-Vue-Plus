package com.xxl.job.admin.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.job.admin.core.model.XxlJobRegistry;
import com.xxl.job.core.util.DateUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobRegistryDao extends BaseMapper<XxlJobRegistry> {

    default List<Integer> findDead(@Param("timeout") int timeout,
                                   @Param("nowTime") Date nowTime) {
        long time = nowTime.getTime() - (timeout * 1000L);
        List<XxlJobRegistry> list = this.selectList(new LambdaQueryWrapper<XxlJobRegistry>()
            .select(XxlJobRegistry::getId)
            .lt(XxlJobRegistry::getUpdateTime, DateUtil.formatDateTime(new Date(time))));
        return CollUtil.isEmpty(list) ? new ArrayList<>() : list.stream().map(XxlJobRegistry::getId).collect(Collectors.toList());
    }

    default int removeDead(@Param("ids") List<Integer> ids) {
        return this.deleteBatchIds(ids);
    }

    default List<XxlJobRegistry> findAll(@Param("timeout") int timeout,
                                        @Param("nowTime") Date nowTime) {
        long time = nowTime.getTime() - (timeout * 1000L);
        return this.selectList(new LambdaQueryWrapper<XxlJobRegistry>()
            .gt(XxlJobRegistry::getUpdateTime, DateUtil.formatDateTime(new Date(time))));
    }

    default int registryUpdate(@Param("registryGroup") String registryGroup,
                               @Param("registryKey") String registryKey,
                               @Param("registryValue") String registryValue,
                               @Param("updateTime") Date updateTime) {
        return this.update(null, new LambdaUpdateWrapper<XxlJobRegistry>()
            .set(XxlJobRegistry::getUpdateTime, updateTime)
            .eq(XxlJobRegistry::getRegistryGroup, registryGroup)
            .eq(XxlJobRegistry::getRegistryKey, registryKey)
            .eq(XxlJobRegistry::getRegistryValue, registryValue));
    }

    default int registrySave(@Param("registryGroup") String registryGroup,
                             @Param("registryKey") String registryKey,
                             @Param("registryValue") String registryValue,
                             @Param("updateTime") Date updateTime) {
        XxlJobRegistry xxlJobRegistry = new XxlJobRegistry();
        xxlJobRegistry.setRegistryGroup(registryGroup);
        xxlJobRegistry.setRegistryKey(registryKey);
        xxlJobRegistry.setRegistryValue(registryValue);
        xxlJobRegistry.setUpdateTime(updateTime);
        return this.insert(xxlJobRegistry);
    }

    default int registryDelete(@Param("registryGroup") String registryGroup,
                               @Param("registryKey") String registryKey,
                               @Param("registryValue") String registryValue) {
        return this.delete(new LambdaQueryWrapper<XxlJobRegistry>()
            .eq(XxlJobRegistry::getRegistryGroup, registryGroup)
            .eq(XxlJobRegistry::getRegistryKey, registryKey)
            .eq(XxlJobRegistry::getRegistryValue, registryValue));
    }

}
