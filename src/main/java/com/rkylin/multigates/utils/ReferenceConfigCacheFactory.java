package com.rkylin.multigates.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;

/**
 * Created by 嘉玮 on 2016-7-19.
 */
public class ReferenceConfigCacheFactory {

    private static ReferenceConfigCache cache = ReferenceConfigCache.getCache("_MuliGates_", new ReferenceConfigCache.KeyGenerator() {
        @Override
        public String generateKey(ReferenceConfig<?> referenceConfig) {
            String iName = referenceConfig.getInterface();
            if(StringUtils.isBlank(iName)) {
                Class<?> clazz = referenceConfig.getInterfaceClass();
                iName = clazz.getName();
            }
            if(StringUtils.isBlank(iName)) {
                throw new IllegalArgumentException("No interface info in ReferenceConfig" + referenceConfig);
            }

            StringBuilder ret = new StringBuilder();
            if(! StringUtils.isBlank(referenceConfig.getUrl())){
                ret.append(referenceConfig.getUrl()).append(":");
            }
            if(! StringUtils.isBlank(referenceConfig.getGroup())) {
                ret.append(referenceConfig.getGroup()).append("/");
            }
            ret.append(iName);
            if(! StringUtils.isBlank(referenceConfig.getVersion())) {
                ret.append(":").append(referenceConfig.getVersion());
            }

            return ret.toString();
        }
    });

    public static ReferenceConfigCache get(){
        return cache ;
    }
}
