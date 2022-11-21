package org.originit.common.file;

import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;

/**
 * @author xxc
 */
public abstract class AbstractSimpleFileUploadStrategy implements FileUploadStrategy {

    @Override
    public UploadResult uploadImage(FileInfo file, UploadConfig config) {
        return justUpload(file,config);
    }

    @Override
    public UploadResult uploadFile(FileInfo file, UploadConfig config) {
        return justUpload(file,config);
    }

    @Override
    public UploadResult uploadBigFile(FileInfo file, UploadConfig config) {
        return justUpload(file,config);
    }

    protected abstract UploadResult justUpload(FileInfo file, UploadConfig config);

}
