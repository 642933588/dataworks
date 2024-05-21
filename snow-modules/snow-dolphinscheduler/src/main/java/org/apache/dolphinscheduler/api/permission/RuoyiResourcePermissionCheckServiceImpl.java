/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.api.permission;

import com.snow.common.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class snowResourcePermissionCheckServiceImpl extends ResourcePermissionCheckServiceImpl {

    @Override
    public boolean operationPermissionCheck(Object authorizationType, Integer userId,
                                            String permissionKey, Logger logger) {
        if (SecurityUtils.isAdmin(userId.longValue())) {
            return true;
        }
        return RESOURCE_LIST_MAP.get(authorizationType).permissionCheck(userId, permissionKey, logger);
    }

    @Override
    public boolean functionDisabled() {
        return false;
    }

    @Override
    public Set<Object> userOwnedResourceIdsAcquisition(Object authorizationType, Integer userId, Logger logger) {
        return (Set<Object>) RESOURCE_LIST_MAP.get(authorizationType).listAuthorizedResourceIds(
                SecurityUtils.isAdmin(userId.longValue()) ? 0 : userId, logger);
    }

}
