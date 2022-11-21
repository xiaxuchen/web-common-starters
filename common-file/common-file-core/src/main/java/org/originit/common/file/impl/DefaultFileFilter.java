package org.originit.common.file.impl;

import org.originit.common.file.FileFilter;
import org.originit.common.file.entity.ContextInfo;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.property.FileProperties;

/**
 * 默认的过滤器
 * @author xxc
 */
public class DefaultFileFilter implements FileFilter {

    FileProperties properties;

    public DefaultFileFilter(FileProperties properties) {
        this.properties = properties;
    }

    @Override
    public void doFilter(FileInfo file, UploadConfig config, ContextInfo info) {
        String suffix = info.getSuffix();
        if (info.getIsImg()) {
            if (properties.getSupportImgs() != null && !properties.getSupportImgs().isEmpty()) {
                boolean isSupportAll = properties.getSupportImgs().size() == 1 && properties.getSupportImgs().contains(FileFilter.SUPPORT_ALL_IMAGES);
                if (!isSupportAll && !properties.getSupportImgs().contains(suffix.toLowerCase())) {
                    throw new IllegalArgumentException("不支持图片类型" + suffix + "!");
                } else if(file.getSize() > properties.getImageMaxSize()) {
                    throw new IllegalArgumentException("图片尺寸太大!!");
                }
            } else {
                throw new IllegalArgumentException("不支持图片文件!");
            }
        }
    }

}
