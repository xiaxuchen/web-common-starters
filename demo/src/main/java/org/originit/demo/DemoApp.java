package org.originit.demo;

import org.originit.common.file.FileUploaderDeprecated;
import org.originit.common.file.entity.FileInfo;
import org.originit.common.file.entity.UploadConfig;
import org.originit.common.file.entity.UploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SpringBootApplication
@RestController
public class DemoApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class,args);
    }

    @Autowired
    FileUploaderDeprecated uploader;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        final FileInfo fileInfo = FileInfo.builder()
                .isEmpty(file.isEmpty())
                .contentType(file.getContentType())
                .inputStream(file.getInputStream())
                .name(file.getName())
                .size(file.getSize())
                .originalFileName(file.getOriginalFilename())
                .build();
        final UploadConfig config = new UploadConfig();
        config.setModule("test/111");
        final UploadResult upload = uploader.upload(fileInfo, config);
        return upload == null?"上传失败":upload.getPath();
    }

}
