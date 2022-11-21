package org.originit.common.file.dfs;

import org.originit.common.file.AbstractSimpleFileUploadStrategy;
import org.originit.common.file.dfs.util.FastDfsUtil;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;
import org.springframework.stereotype.Component;

/**
 * @author xxc
 */
@Component
public class DFSUploadStrategy extends AbstractSimpleFileUploadStrategy {

    public static final String TYPE = "DFS";


    @Override
    protected UploadResult justUpload(FileInfo file, UploadConfig config) {
        try {
            return UploadResult.success(FastDfsUtil.uploadCommonFile(file));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
