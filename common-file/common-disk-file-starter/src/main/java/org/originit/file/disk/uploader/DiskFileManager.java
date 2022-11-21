package org.originit.file.disk.uploader;

import com.originit.common.utils.FileUDUtil;
import org.originit.common.file.FileManager;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiskFileManager implements FileManager {

    @Autowired
    org.originit.file.disk.property.UploadConfig fileConfig;

    public static final UploadConfig DEFAULT_CONFIG = UploadConfig.builder().module("default").tag("default").build();

    @Override
    public UploadResult upload(FileInfo fileInfo, UploadConfig config) {
        if (config == null) {
            config = DEFAULT_CONFIG;
        }
        final String folder = new StringBuilder().append(fileConfig.getRootPath())
                .append("/").append(config.getModule()==null?DEFAULT_CONFIG.getModule():config.getModule()).append("/")
                .append(config.getTag()==null?DEFAULT_CONFIG.getTag():config.getTag()).append("/").toString() ;
        return UploadResult.success(FileUDUtil.saveFile(fileInfo.getInputStream(),
                folder, fileInfo.getOriginalFileName()));
    }

    @Override
    public File getFile(String path) {
        return FileUDUtil.getFile(path);
    }

    @Override
    public void deleteFile(String path) {
        final File file = getFile(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
