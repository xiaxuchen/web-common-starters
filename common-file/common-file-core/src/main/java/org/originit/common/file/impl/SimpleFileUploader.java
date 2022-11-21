package org.originit.common.file.impl;

import com.originit.common.utils.StringUtil;
import org.originit.common.file.AbstractFileUploader;
import org.originit.common.file.FileUploadStrategy;
import org.originit.common.file.entity.ContextInfo;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;
import org.originit.common.file.property.FileProperties;

/**
 * @author xxc
 */
public class SimpleFileUploader extends AbstractFileUploader {

    FileProperties properties;

    public SimpleFileUploader(FileProperties fileProperties) {
        this.properties = fileProperties;
    }

    @Override
    protected UploadResult onUpload(FileUploadStrategy strategy, FileInfo file, UploadConfig config, ContextInfo info) {
        String suffix = info.getSuffix();
        Boolean isImg = info.getIsImg();
        if (!StringUtil.isEmpty(suffix)) {
            if (isImg) {
                return strategy.uploadImage(file,config);
            }
        }
        if (file.getSize() >= properties.getBigSize()) {
            return strategy.uploadBigFile(file, config);
        } else {
            return strategy.uploadFile(file, config);
        }
    }
}
