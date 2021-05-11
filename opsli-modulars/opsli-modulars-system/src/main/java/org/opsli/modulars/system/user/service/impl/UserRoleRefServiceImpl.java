/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.modulars.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.ListDistinctUtil;
import org.opsli.core.msg.CoreMsg;
import org.opsli.core.utils.UserUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.user.entity.SysUserRoleRef;
import org.opsli.modulars.system.user.mapper.UserRoleRefMapper;
import org.opsli.modulars.system.user.service.IUserRoleRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 角色 接口实现类
 */
@Service
public class UserRoleRefServiceImpl extends ServiceImpl<UserRoleRefMapper, SysUserRoleRef> implements IUserRoleRefService {

    @Autowired(required = false)
    private UserRoleRefMapper mapper;

    @Override
    public List<String> getUserIdListByRoleId(String roleId) {
        List<String> users = mapper.getUserIdListByRoleId(roleId);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    public List<String> getUserIdListByMenuId(String roleId) {
        List<String> users = mapper.getUserIdListByMenuId(roleId);
        if(CollUtil.isEmpty(users)){
            return ListUtil.empty();
        }

        // 去重
        return ListDistinctUtil.distinct(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setRoles(String userId, String[] roleIds) {
        if(StringUtils.isEmpty(userId)){
            throw new ServiceException(SystemMsg.EXCEPTION_USER_ID_NOT_NULL);
        }

        // 删除已有权限
        String userIdField = "user_id";
        QueryWrapper<SysUserRoleRef> wrapper = new QueryWrapper<>();
        wrapper.eq(userIdField, userId);
        super.remove(wrapper);

        if(roleIds != null && roleIds.length > 0){
            List<SysUserRoleRef> list = Lists.newArrayListWithCapacity(roleIds.length);
            for (String roleId : roleIds) {
                SysUserRoleRef entity = new SysUserRoleRef();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                list.add(entity);
            }
            boolean ret = super.saveBatch(list);
            if(ret){
                // 清除缓存
                this.clearCache(userId);
            }
        }

        return true;
    }


    // ===========

    /**
     * 清除缓存
     * @param userId
     */
    private void clearCache(String userId) {
        int cacheCount = 3;
        boolean tmp;
        // 清空当期用户缓存角色、权限、菜单
        tmp = UserUtil.refreshUserRoles(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserAllPerms(userId);
        if(tmp){
            cacheCount--;
        }
        tmp = UserUtil.refreshUserMenus(userId);
        if(tmp){
            cacheCount--;
        }

        // 判断删除状态
        if(cacheCount != 0){
            // 删除缓存失败
            throw new ServiceException(CoreMsg.CACHE_DEL_EXCEPTION);
        }
    }
}


