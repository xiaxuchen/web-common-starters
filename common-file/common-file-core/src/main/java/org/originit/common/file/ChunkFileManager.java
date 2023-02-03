package org.originit.common.file;

import org.springframework.web.multipart.MultipartFile;

public interface ChunkFileManager {

    boolean checkChunkFile(String path, String chunk, String chunkSize);

    void uploadChunkFile(String path, String chunk, String chunkSize, MultipartFile file);

    void mergeChunkFile(String path, String chunk, String chunkSize, String fileName);
}
