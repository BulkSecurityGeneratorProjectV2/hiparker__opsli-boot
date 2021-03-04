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
package org.opsli.modulars.system.area.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.modulars.system.area.entity.SysArea;

import java.util.List;

/**
* @BelongsProject: opsli-boot
* @BelongsPackage: org.opsli.modulars.system.area.mapper
* @Author: Parker
* @CreateTime: 2020-11-28 18:59:59
* @Description: 地域表 Mapper
*/
@Mapper
public interface SysAreaMapper extends BaseMapper<SysArea> {


    /**
     * 是否有下级
     * @return List
     */
    List<HasChildren> hasChildren(@Param(Constants.WRAPPER) Wrapper wrapper);

}
