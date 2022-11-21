package org.originit.common.file.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@ConfigurationProperties("common.file")
public class FileProperties {

    /**
     * 大尺寸文件的界限
     */
    @Getter
    @Setter
    private long bigSize = 10 * 1024 * 1024;

    /**
     * 图片最大大小
     */
    @Getter
    @Setter
    private long imageMaxSize = 8 * 1024 * 1024;

    /**
     * 支持的图片类型
     */
    @Setter
    private List<String> supportImgs;

    private Set<String> supportImgSet;

    private boolean handled = false;

    public Set<String> getSupportImgs() {
        if (!handled && supportImgs != null && !supportImgs.isEmpty()) {
            supportImgSet = supportImgs.stream().map(String::toLowerCase).collect(Collectors.toSet());
            handled = true;
        }
        return supportImgSet;
    }


}
