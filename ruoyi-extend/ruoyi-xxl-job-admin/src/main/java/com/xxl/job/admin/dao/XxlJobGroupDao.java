package com.xxl.job.admin.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.admin.core.model.XxlJobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobGroupDao extends BaseMapper<XxlJobGroup> {

    default List<XxlJobGroup> findAll() {
        return this.selectList(
            new LambdaQueryWrapper<XxlJobGroup>()
                .orderByAsc(XxlJobGroup::getAppname)
                .orderByAsc(XxlJobGroup::getTitle)
                .orderByAsc(XxlJobGroup::getId));
    }

    default List<XxlJobGroup> findByAddressType(@Param("addressType") int addressType) {
        return this.selectList(
            new LambdaQueryWrapper<XxlJobGroup>()
                .eq(XxlJobGroup::getAddressType, addressType)
                .orderByAsc(XxlJobGroup::getAppname)
                .orderByAsc(XxlJobGroup::getTitle)
                .orderByAsc(XxlJobGroup::getId));
    }

    default int save(XxlJobGroup xxlJobGroup) {
        return this.insert(xxlJobGroup);
    }

    default int update(XxlJobGroup xxlJobGroup) {
        return this.updateById(xxlJobGroup);
    }

    default int remove(@Param("id") int id) {
        return this.deleteById(id);
    }

    default XxlJobGroup load(@Param("id") int id) {
        return this.selectById(id);
    }

    default Page<XxlJobGroup> pageList(@Param("offset") int offset,
                                       @Param("pagesize") int pagesize,
                                       @Param("appname") String appname,
                                       @Param("title") String title) {
        return this.selectPage(new Page<>(offset, pagesize),
            new LambdaQueryWrapper<XxlJobGroup>()
                .like(StrUtil.isNotBlank(appname), XxlJobGroup::getAppname, appname)
                .like(StrUtil.isNotBlank(title), XxlJobGroup::getTitle, title)
                .orderByAsc(XxlJobGroup::getAppname)
                .orderByAsc(XxlJobGroup::getTitle)
                .orderByAsc(XxlJobGroup::getId));
    }

//    public int pageListCount(@Param("offset") int offset,
//                             @Param("pagesize") int pagesize,
//                             @Param("appname") String appname,
//                             @Param("title") String title);

}
