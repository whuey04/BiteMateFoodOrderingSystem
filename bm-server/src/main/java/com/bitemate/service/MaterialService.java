package com.bitemate.service;

import org.springframework.web.multipart.MultipartFile;

public interface MaterialService {
    /**
     * Upload image file
     * @param file
     * @return
     */
    String uploadFile(MultipartFile file);
}
