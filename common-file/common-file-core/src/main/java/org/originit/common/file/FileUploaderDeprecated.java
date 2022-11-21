package org.originit.common.file;

import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;

/**
 * 文件上传器
 */
@Deprecated
public interface FileUploaderDeprecated {

    UploadResult upload(FileInfo file, UploadConfig config);

}