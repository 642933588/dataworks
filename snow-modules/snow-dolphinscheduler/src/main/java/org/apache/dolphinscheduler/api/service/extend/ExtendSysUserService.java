package org.apache.dolphinscheduler.api.service.extend;

import org.apache.dolphinscheduler.dao.entity.User;

import java.util.List;

public interface ExtendSysUserService {

    User selectById(Integer userId);

    List<User> selectList(Object obj);

    User queryByUserNameAccurately(String username);
}
