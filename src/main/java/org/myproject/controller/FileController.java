package org.myproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myproject.vo.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    @Value("${upload.path:uploads/}")
    private String uploadPath;

    @PostMapping("/upload")
    public ResultVO<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultVO.error(400, "请选择要上传的文件");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            String newFileName = UUID.randomUUID().toString().replace("-", "") + extension;
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File destFile = new File(uploadPath + newFileName);
            file.transferTo(destFile);
            
            log.info("文件上传成功: {}", destFile.getAbsolutePath());
            
            Map<String, String> result = new HashMap<>();
            result.put("url", "/uploads/" + newFileName);
            result.put("filename", newFileName);
            
            return ResultVO.success(result);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResultVO.error(500, "文件上传失败: " + e.getMessage());
        }
    }
}