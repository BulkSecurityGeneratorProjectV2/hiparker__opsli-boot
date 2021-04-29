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

package org.opsli.modulars.system.options.service;


import org.opsli.core.base.service.interfaces.CrudServiceInterface;



import org.opsli.modulars.system.options.entity.SysOptions;
import org.opsli.api.wrapper.system.options.OptionsModel;

import java.util.Map;


/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.options.service
* @Author: Parker
* @CreateTime: 2021-02-07 18:24:38
* @Description: 系统参数 Service
*/
public interface ISysOptionsService extends CrudServiceInterface<SysOptions, OptionsModel> {

    /**
     * 更新参数
     * @param params
     */
    void updateOptions(Map<String, String> params);

}
