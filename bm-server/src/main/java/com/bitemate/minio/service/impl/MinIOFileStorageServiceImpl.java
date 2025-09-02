package com.bitemate.minio.service.impl;

import com.bitemate.minio.config.MinIOConfig;
import com.bitemate.minio.config.MinIOConfigProperties;
import com.bitemate.minio.service.FileStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@Import(MinIOConfig.class)
@EnableConfigurationProperties(MinIOConfigProperties.class)
public class MinIOFileStorageServiceImpl implements FileStorageService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    private final static String separator = "/";

    public String buildFilePath(String dirPath, String fileName) {
        StringBuilder strBuilder = new StringBuilder(50);
        if(!StringUtils.isEmpty(dirPath)) {
            strBuilder.append(dirPath).append(separator);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = simpleDateFormat.format(new Date());
        strBuilder.append(todayStr).append(separator);
        strBuilder.append(fileName);
        return strBuilder.toString();
    }
    /**
     * Upload image file
     *
     * @param prefix
     * @param filename
     * @param inputStream
     * @return
     */
    @Override
    public String uploadImgFile(String prefix, String filename, InputStream inputStream) {
        String filePath = buildFilePath(prefix, filename);
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType("image/jpeg")
                    .bucket(minIOConfigProperties.getBucket())
                    .stream(inputStream, inputStream.available(), -1)
                    .build();

            minioClient.putObject(objectArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator + minIOConfigProperties.getBucket());
            urlPath.append(separator);
            urlPath.append(filePath);
            return urlPath.toString();

        }catch (Exception e) {
            log.error("minio put file error.",e);
            throw new RuntimeException("Upload file failed.");
        }
    }

    /**
     * Download file
     *
     * @param pathUrl
     * @return
     */
    @Override
    public byte[] downLoadFile(String pathUrl) {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint()+"/","");
        int index = key.indexOf(separator);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index+1);
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minIOConfigProperties.getBucket())
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            log.error("minio down file error.  pathUrl:{}",pathUrl);
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int rc = 0;
        while (true){
            try {
                if (!((rc = inputStream.read(buffer,0,100)) > 0)){
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayOutputStream.write(buffer,0,rc);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Delete file
     *
     * @param pathUrl
     */
    @Override
    public void delete(String pathUrl) {
        String prefix = minIOConfigProperties.getReadPath()
                + "/" + minIOConfigProperties.getBucket() + "/";
        String fileName = pathUrl.replace(prefix, "");

        //Remove obj
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(minIOConfigProperties.getBucket())
                .object(fileName)
                .build();
        try {
            minioClient.removeObject(removeObjectArgs);
            log.info("minio delete file success. ");
        }catch (Exception e) {
            log.error("minio remove file error.  pathUrl:{}",pathUrl);
            e.printStackTrace();
        }
    }
}
