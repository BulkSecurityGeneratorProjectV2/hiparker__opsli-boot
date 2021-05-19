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
package org.opsli.core.cache.pushsub.handler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opsli.common.constants.CacheConstants;
import org.opsli.core.cache.local.CacheUtil;
import org.opsli.core.cache.pushsub.enums.CacheHandleType;
import org.opsli.core.cache.pushsub.enums.MsgArgsType;
import org.opsli.core.cache.pushsub.enums.PushSubType;
import org.opsli.plugins.cache.EhCachePlugin;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 热数据处理
 *
 * @author Parker
 * @date 2020-09-16
 */
@Slf4j
public class HotDataHandler implements RedisPushSubHandler{

    @Autowired
    private EhCachePlugin ehCachePlugin;

    @Override
    public PushSubType getType() {
        return PushSubType.HOT_DATA;
    }

    @Override
    public void handler(JSONObject msgJson) {
        String key = (String) msgJson.get(MsgArgsType.CACHE_DATA_KEY.toString());
        Object value = msgJson.get(MsgArgsType.CACHE_DATA_VALUE.toString());
        CacheHandleType type = CacheHandleType.valueOf((String )msgJson.get(MsgArgsType.CACHE_DATA_TYPE.toString()));

        if(StringUtils.isEmpty(key)){
            return;
        }

        // 拼装缓存key
        String cacheName = CacheUtil.handleKey(CacheConstants.HOT_DATA_PREFIX +":"+ key);

        if(CacheHandleType.UPDATE == type){
            ehCachePlugin.put(CacheConstants.EHCACHE_SPACE, cacheName, value);
        }
        // 缓存删除
        else if(CacheHandleType.DELETE == type){
            ehCachePlugin.delete(CacheConstants.EHCACHE_SPACE, cacheName);
        }
    }

}
