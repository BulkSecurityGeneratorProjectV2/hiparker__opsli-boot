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
package org.opsli.modulars.creater.createrlogs.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.menu.MenuApi;
import org.opsli.api.wrapper.system.menu.MenuFullModel;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.creater.exception.CreaterException;
import org.opsli.core.creater.msg.CreaterMsg;
import org.opsli.core.creater.strategy.create.CodeBuilder;
import org.opsli.core.persistence.Page;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.modulars.creater.column.service.ITableColumnService;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.modulars.creater.createrlogs.entity.CreaterLogs;
import org.opsli.modulars.creater.createrlogs.mapper.CreaterLogsMapper;
import org.opsli.modulars.creater.createrlogs.service.ICreateLogsService;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterBuilderModel;
import org.opsli.modulars.creater.createrlogs.wrapper.CreaterLogsModel;
import org.opsli.modulars.creater.table.service.ITableService;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;
import org.opsli.modulars.creater.table.wrapper.CreaterTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 代码生成器 - 日志 接口实现类
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
@Service
public class CreateLogsServiceImpl extends CrudServiceImpl<CreaterLogsMapper, CreaterLogs, CreaterLogsModel>
        implements ICreateLogsService {

    @Autowired(required = false)
    private CreaterLogsMapper mapper;
    @Autowired
    private ITableService iTableService;
    @Autowired
    private ITableColumnService iTableColumnService;
    @Autowired
    private MenuApi menuApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableId(String tableId) {
        QueryBuilder<CreaterLogs> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<CreaterLogs> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);
        super.remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByTableIds(String[] tableIds) {
        if(tableIds != null){
            for (String tableId : tableIds) {
                QueryBuilder<CreaterLogs> queryBuilder =
                        new GenQueryBuilder<>();
                QueryWrapper<CreaterLogs> wrapper = queryBuilder.build();
                wrapper.eq("table_id", tableId);
                super.remove(wrapper);
            }
        }
    }

    @Override
    public CreaterLogsModel getByTableId(String tableId) {
        QueryBuilder<CreaterLogs> queryBuilder =
                new GenQueryBuilder<>();
        QueryWrapper<CreaterLogs> wrapper = queryBuilder.build();
        wrapper.eq("table_id", tableId);

        CreaterLogsModel model = null;
        Page<CreaterLogs, CreaterLogsModel> page = new Page<>(1,1);
        page.setQueryWrapper(wrapper);
        page = this.findPage(page);
        if(page != null && CollUtil.isNotEmpty(page.getList())){
            model = page.getList().get(0);
        }

        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreaterLogsModel model, HttpServletResponse response) {
        if(model == null){
            // 生成失败，数据为空
            throw new CreaterException(CreaterMsg.EXCEPTION_CREATE_NULL);
        }
        model.setVersion(null);
        CreaterLogsModel saveModel = this.save(model);
        if(saveModel != null){
            CreaterTableModel createrTableModel = iTableService.get(saveModel.getTableId());
            if(createrTableModel == null){
                // 生成失败，暂无表数据
                throw new CreaterException(CreaterMsg.EXCEPTION_CREATE_TABLE_NULL);
            }

            CreaterTableAndColumnModel currTableModel = WrapperUtil.transformInstance(
                    createrTableModel, CreaterTableAndColumnModel.class
            );
            List<CreaterTableColumnModel> columnModelList = iTableColumnService.
                    getByTableId(currTableModel.getId());
            if(columnModelList == null || columnModelList.isEmpty()){
                // 生成失败，暂无表字段
                throw new CreaterException(CreaterMsg.EXCEPTION_CREATE_FIELD_NULL);
            }


            // 赋值表字段
            currTableModel.setColumnList(columnModelList);

            // 生成代码
            CreaterBuilderModel builderModel = WrapperUtil.transformInstance(
                    saveModel, CreaterBuilderModel.class
            );
            builderModel.setModel(currTableModel);

            // 生成代码
            CodeBuilder.INSTANCE.build(builderModel, response);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMenu(String menuParentId, String tableId) {
        if(StringUtils.isEmpty(menuParentId)){
            // 上级菜单不可为空
            throw new CreaterException(CreaterMsg.EXCEPTION_CREATE_MENU_PARENT_NULL);
        }

        CreaterLogsModel byTableId = this.getByTableId(tableId);
        if(byTableId == null){
            // 生成菜单失败，请先 生成代码
            throw new CreaterException(CreaterMsg.EXCEPTION_CREATE_MENU_CODE_NULL);
        }

        // 完整新增菜单
        MenuFullModel menuFullModel = new MenuFullModel();
        menuFullModel.setParentId(menuParentId);
        menuFullModel.setTitle(byTableId.getCodeTitle());
        menuFullModel.setModuleName(byTableId.getModuleName());
        menuFullModel.setSubModuleName(byTableId.getSubModuleName());
        ResultVo<?> insertRet = menuApi.saveMenuByFull(menuFullModel);

        return insertRet.isSuccess();
    }


}


