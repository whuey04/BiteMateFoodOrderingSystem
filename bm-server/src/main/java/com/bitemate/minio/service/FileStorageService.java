package com.bitemate.minio.service;

import java.io.InputStream;

public interface FileStorageService {

    /**
     * Upload image file
     * @param prefix
     * @param filename
     * @param inputStream
     * @return
     */
    String uploadImgFile(String prefix, String filename, InputStream inputStream);

    /**
     * Download file
     * @param pathUrl
     * @return
     */
    byte[]  downLoadFile(String pathUrl);

    /**
     * Delete file
     * @param pathUrl
     */
    void delete(String pathUrl);
}
