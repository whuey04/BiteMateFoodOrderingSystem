package com.bitemate.controller.admin;

import com.bitemate.result.Result;
import com.bitemate.service.MaterialService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Update and download file
 */
@Api(tags = "Admin - Common Controller")
@RestController
@Slf4j
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private MaterialService materialService;

    /**
     * Upload file
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) {
        String filePath = materialService.uploadFile(file);
        return Result.success(filePath);
    }
}
