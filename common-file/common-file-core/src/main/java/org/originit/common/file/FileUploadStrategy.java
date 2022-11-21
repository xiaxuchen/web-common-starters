package org.originit.common.file;

import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;

public interface FileUploadStrategy {

    long BIG_SIZE_GLOBAL = -1;

    /**
     * 上传图片
     * @param file 文件相关信息
     * @param config 上传相关配置
     * @return 上传结果
     */
    UploadResult uploadImage(FileInfo file, UploadConfig config);


    /**
     * 上传普通文件
     * @param file 文件相关信息
     * @param config 上传相关配置
     * @return 上传结果
     */
    UploadResult uploadFile(FileInfo file,UploadConfig config);

    /**
     * 上传大文件
     * @param file 文件相关信息
     * @param config 上传相关配置
     * @return 上传结果
     */
    UploadResult uploadBigFile(FileInfo file,UploadConfig config);

    /**
     * 唯一文件上传策略值
     * @return 当前上传策略名称
     */
    String getType();

    /**
     * 区分大文件的大小
     * @return 默认是使用全局配置
     */
    default long getBigSize() {
        return BIG_SIZE_GLOBAL;
    }
}