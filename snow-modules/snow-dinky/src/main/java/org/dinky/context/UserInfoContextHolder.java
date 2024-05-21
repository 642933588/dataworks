/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.dinky.context;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.snow.common.security.utils.SecurityUtils;
import com.snow.system.api.model.LoginUser;
import org.dinky.data.dto.UserDTO;
import org.dinky.data.model.rbac.Menu;
import org.dinky.data.model.rbac.Role;
import org.dinky.data.model.rbac.Tenant;
import org.dinky.data.model.rbac.User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** UserInfoContextHolder */
public class UserInfoContextHolder {

    private static final Map<Integer, UserDTO> USER_INFO = new ConcurrentHashMap<>();

    public static void set(Integer userId, UserDTO userInfo) {
        USER_INFO.put(userId, userInfo);
    }

    public static void remove(Integer userId) {
        USER_INFO.remove(userId);
    }

    public static UserDTO get(Integer userId) {
        return USER_INFO.get(userId);
    }
    public static UserDTO get() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = new User();
        user.setUsername(loginUser.getUsername());
        user.setNickname(loginUser.getSysUser().getNickName());
        user.setId(loginUser.getUserid().intValue());
        //TODO 判断是否超级用户
        user.setSuperAdminFlag(true);

        Tenant currentTenant = new Tenant();
        currentTenant.setTenantCode("000000");
        currentTenant.setId(1);

        Set<String> rolePermission = loginUser.getRoles();
        List<Role> roleList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(rolePermission)){

            rolePermission.forEach(e->{
                Role role = new Role();
                role.setRoleCode(e);
                role.setTenantId(currentTenant.getId());
                roleList.add(role);
            });

        }

        Set<String> menuPermission = loginUser.getPermissions();
        List<Menu> menuList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(menuPermission)) {
            menuPermission.forEach(e->{
                Menu menu = new Menu();
                menu.setPerms(e);
                menu.setParentId(2);
                menuList.add(menu);
            });
        }

        UserDTO dto = new UserDTO();
        dto.setUser(user);
        dto.setCurrentTenant(currentTenant);
//        dto.setTokenInfo(StpUtil.getTokenInfo());
        dto.setRoleList(roleList);
        dto.setMenuList(menuList);
        return dto;
    }

    public static void refresh(Integer userId, UserDTO userInfo) {
        set(userId, userInfo);
    }
}
