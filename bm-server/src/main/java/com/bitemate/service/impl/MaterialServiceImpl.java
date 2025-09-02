package com.bitemate.service.impl;

import com.bitemate.constant.MessageConstant;
import com.bitemate.exception.BaseException;
import com.bitemate.minio.service.FileStorageService;
import com.bitemate.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Upload image file
     *
     * @param file
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file) {
        if(file == null || file.getSize() == 0){
            throw new BaseException(MessageConstant.PARAM_INVALID);
        }

        // Upload image file to minIO
        String fileName = UUID.randomUUID().toString().replace("-", "");
        String originalFilename = file.getOriginalFilename();
        String postfix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filePath = null;
        try {
            filePath = fileStorageService.uploadImgFile("", fileName + postfix, file.getInputStream());
            log.info("Upload file to MinIO，filePath:{}",filePath);
            return filePath;

        } catch (IOException e) {
            throw new BaseException("Failed to upload file to MinIO");
        }
    }
}
