package org.originit.common.file;

import org.originit.common.file.entity.ContextInfo;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;

/**
 * 文件过滤器，排除掉不符合条件的类
 */
public interface FileFilter {

    String SUPPORT_ALL_IMAGES = "*";

    /**
     * 过滤不合法的文件
     *
     * @param file   上传的文件
     * @param config 文件相关配置
     * @param info
     * @return 是否合法
     */
    void doFilter(FileInfo file, UploadConfig config, ContextInfo info);

}
