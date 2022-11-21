package org.originit.common.file;

import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileManager {
    UploadResult upload(FileInfo fileInfo, UploadConfig config);

    default UploadResult upload(FileInfo fileInfo) {
        return this.upload(fileInfo, null);
    }

    default UploadResult upload(MultipartFile multipartFile, UploadConfig config) {
        try {
            return this.upload(FileInfo.builder().size(multipartFile.getSize()).name(multipartFile.getName())
                    .inputStream(multipartFile.getInputStream())
                    .contentType(multipartFile.getContentType())
                    .isEmpty(multipartFile.isEmpty())
                    .originalFileName(multipartFile.getOriginalFilename())
                    .build(), config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default UploadResult upload(MultipartFile multipartFile) {
        return this.upload(multipartFile, null);
    }

    /**
     * 获取文件
     * @param path 文件code
     * @return
     */
    File getFile(String path);

    /**
     * 删除文件
     * @param path
     */
    void deleteFile(String path);
}
