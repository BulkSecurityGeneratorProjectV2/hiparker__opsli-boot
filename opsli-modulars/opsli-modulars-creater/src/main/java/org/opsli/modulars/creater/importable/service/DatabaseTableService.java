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
package org.opsli.modulars.creater.importable.service;

import org.opsli.core.creater.enums.DataBaseType;
import org.opsli.modulars.creater.importable.entity.DatabaseColumn;
import org.opsli.modulars.creater.importable.entity.DatabaseTable;

import java.util.List;


/**
 * 代码生成器 - 数据库表
 *
 * @author parker
 * @date 2020-09-16 17:34
 */
public interface DatabaseTableService {

    /**
     * 获得类型
     * @return 类型
     */
    DataBaseType getType();

    /**
     * 获得当前库中 数据库名
     * @param dbName 表名
     * @return List
     */
    List<DatabaseTable> findTables(String dbName);

    /**
     * 获得当前库中 所有表
     * @param dbName 数据库名
     * @param tableName 表名
     * @return List
     */
    List<DatabaseTable> findTables(String dbName, String tableName);

    /**
     * 获得当前表中 所有字段
     * @param dbName 数据库名
     * @param tableName 表名
     * @return List
     */
    List<DatabaseColumn> findColumns(String dbName, String tableName);

}
