package com.xxl.job.admin.dao;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.admin.core.model.XxlJobUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
public interface XxlJobUserDao extends BaseMapper<XxlJobUser> {

    default Page<XxlJobUser> pageList(@Param("offset") int offset,
                                     @Param("pagesize") int pagesize,
                                     @Param("username") String username,
                                     @Param("role") int role) {
        return this.selectPage(new Page<>(offset, pagesize),
            new LambdaQueryWrapper<XxlJobUser>()
                .like(StrUtil.isNotBlank(username), XxlJobUser::getUsername, username)
                .eq(role > -1, XxlJobUser::getRole, role)
                .orderByAsc(XxlJobUser::getUsername));
    }

//    public int pageListCount(@Param("offset") int offset,
//                             @Param("pagesize") int pagesize,
//                             @Param("username") String username,
//                             @Param("role") int role);

    default XxlJobUser loadByUserName(@Param("username") String username) {
        return this.selectOne(new LambdaQueryWrapper<XxlJobUser>().eq(XxlJobUser::getUsername, username));
    }

    default int save(XxlJobUser xxlJobUser) {
        return this.insert(xxlJobUser);
    }

    default int update(XxlJobUser xxlJobUser) {
        return this.update(null,
            new LambdaUpdateWrapper<XxlJobUser>()
                .set(StrUtil.isNotBlank(xxlJobUser.getPassword()), XxlJobUser::getPassword, xxlJobUser.getPassword())
                .set(XxlJobUser::getRole, xxlJobUser.getRole())
                .set(XxlJobUser::getPermission, xxlJobUser.getPermission())
                .eq(XxlJobUser::getId, xxlJobUser.getId()));
    }

    default int delete(@Param("id") int id) {
        return this.deleteById(id);
    }

}
