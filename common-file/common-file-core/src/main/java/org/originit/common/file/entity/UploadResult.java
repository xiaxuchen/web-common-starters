package org.originit.common.file.entity;

import lombok.Data;

@Data
public class UploadResult {

    private String path;

    public static UploadResult success(String path){
        final UploadResult uploadResult = new UploadResult();
        uploadResult.setPath(path);
        return uploadResult;
    }
}
