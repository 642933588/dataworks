package org.apache.dolphinscheduler.api.service.extend;

import com.google.api.client.util.Lists;
import com.snow.common.core.constant.SecurityConstants;
import com.snow.common.core.domain.R;
import com.snow.common.core.exception.ServiceException;
import com.snow.common.core.utils.StringUtils;
import com.snow.common.security.utils.SecurityUtils;
import com.snow.system.api.RemoteUserService;
import com.snow.system.api.domain.SysUser;
import com.snow.system.api.model.LoginUser;
import org.apache.dolphinscheduler.common.constants.TenantConstants;
import org.apache.dolphinscheduler.common.enums.UserType;
import org.apache.dolphinscheduler.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ExtendSysUserServiceImpl implements ExtendSysUserService {

    @Autowired
    private RemoteUserService remoteUserService;

    @Override
    public User selectById(Integer userId) {

        R<SysUser> userResult = remoteUserService.getUserInfoById(userId.longValue(), SecurityConstants.INNER);

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            throw new ServiceException("登录用户：" + userId + " 不存在");
        }

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        SysUser userInfo = userResult.getData();
        return getUserFromSysUser(userInfo);
    }

    @Override
    public List<User> selectList(Object obj) {
        List<User> userList = Lists.newArrayList();
        R<List<SysUser>> listResult = remoteUserService.selectList(SecurityConstants.INNER);

        if (R.FAIL == listResult.getCode()) {
            throw new ServiceException(listResult.getMsg());
        }
        List<SysUser> data = listResult.getData();
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(e -> userList.add(getUserFromSysUser(e)));
        }
        return userList;
    }

    @Override
    public User queryByUserNameAccurately(String username) {
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            throw new ServiceException("登录用户：" + username + " 不存在");
        }

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        SysUser userInfo = userResult.getData().getSysUser();
        return getUserFromSysUser(userInfo);
    }

    private User getUserFromSysUser(SysUser userInfo) {
        User user = new User();
        user.setId(userInfo.getUserId().intValue());
        user.setUserName(userInfo.getUserName());
        user.setUserType(SecurityUtils.isAdmin(userInfo.getUserId()) ? UserType.ADMIN_USER : UserType.GENERAL_USER);
        user.setTenantId(TenantConstants.DEFAULT_TENANT_Id);
        user.setTenantCode(TenantConstants.DEFAULT_TENANT_CODE);
        return user;
    }
}
