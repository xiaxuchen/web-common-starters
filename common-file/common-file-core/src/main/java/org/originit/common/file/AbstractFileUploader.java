package org.originit.common.file;

import com.originit.common.utils.StringUtil;
import org.originit.common.file.entity.ContextInfo;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 抽象
 * @author xxc
 */
public abstract class AbstractFileUploader implements FileUploaderDeprecated {

    protected FileUploadStrategy strategy;

    protected FileFilter filter;

    public final Set<String> IMAGE_TYPES = new HashSet<>(Arrays.asList("bmp","jpg","jpeg","png","gif"));

    public void setStrategy(FileUploadStrategy strategy) {
        this.strategy = strategy;
    }

    public void setFilter(FileFilter filter) {
        this.filter = filter;
    }

    @Override
    public UploadResult upload(FileInfo file, UploadConfig config) {
        if (file == null || file.getSize() <= 0) {
            throw new IllegalArgumentException("文件为空!");
        }
        // 获取文件的后缀以及判断是否是图片
        final String suffix = resolveSuffix(file);
        ContextInfo contextInfo = new ContextInfo();
        contextInfo.setSuffix(suffix);
        contextInfo.setIsImg(IMAGE_TYPES.contains(suffix.toLowerCase()));
        // 过滤不合法的文件
        filter.doFilter(file,config,contextInfo);
        // 选择具体的方法进行上传保存
        return onUpload(strategy,file,config,contextInfo);
    }

    private String resolveSuffix(FileInfo file) {

        int dotIndex = -1;
        if (!StringUtil.isEmpty(file.getOriginalFileName())) {
            dotIndex = file.getOriginalFileName().lastIndexOf(".");
        }
        if (dotIndex != -1) {
            return file.getOriginalFileName().substring(dotIndex + 1);
        }
        return "";
    }

    /**
     * 过滤完成后进行上传
     *
     * @param strategy 上传策略
     * @param file     文件
     * @param config   配置
     * @param info
     * @return 上传结果
     */
    protected abstract UploadResult onUpload(FileUploadStrategy strategy, FileInfo file, UploadConfig config, ContextInfo info);
}
