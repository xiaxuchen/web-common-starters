package org.originit.common.file.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    private String name;

    private String originalFileName;

    private String contentType;

    private Boolean isEmpty;

    private long size;

    private InputStream inputStream;

}
