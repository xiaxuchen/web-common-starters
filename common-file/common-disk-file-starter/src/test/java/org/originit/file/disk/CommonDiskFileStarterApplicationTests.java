package org.originit.file.disk;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.originit.common.file.FileManager;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class CommonDiskFileStarterApplicationTests {

    @Autowired
    FileManager uploader;

    @Test
    void contextLoads() throws FileNotFoundException {
        final UploadResult result = uploader.upload(FileInfo.builder().originalFileName("asdajsd.log")
                .inputStream(new FileInputStream("E:\\ad_log.log")).build());
        Assertions.assertTrue(FileUtil.exist(uploader.getFile(result.getPath())));
    }

}
