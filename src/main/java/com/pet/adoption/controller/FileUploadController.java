package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Tag(name = "文件上传", description = "图片上传相关接口")
@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${app.upload.path:./uploads}")
    private String uploadPath;

    @Value("${app.upload.url-prefix:/uploads}")
    private String urlPrefix;

    @Operation(summary = "上传宠物图片", description = "上传图片并返回访问URL")
    @PostMapping("/pet-image")
    public Result uploadPetImage(@RequestParam("file") MultipartFile file) {
        // 验证文件
        if (file.isEmpty()) {
            return Result.error("请选择要上传的图片");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只支持图片格式（JPG、PNG、GIF等）");
        }

        // 验证文件大小（限制5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error("图片大小不能超过5MB");
        }

        try {
            // 生成文件名：日期 + UUID + 原扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // 验证扩展名
            String lowerExt = extension.toLowerCase();
            if (!lowerExt.matches("\\.(jpg|jpeg|png|gif|webp)$")) {
                return Result.error("只支持 JPG、PNG、GIF、WEBP 格式");
            }

            // 按日期分目录存储
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fileName = UUID.randomUUID().toString().replace("-", "") + extension;
            
            // 创建目录
            Path dirPath = Paths.get(uploadPath, "images", dateDir);
            Files.createDirectories(dirPath);
            
            // 保存文件
            Path filePath = dirPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            
            // 生成访问URL
            String imageUrl = urlPrefix + "/images/" + dateDir + "/" + fileName;
            
            return Result.success("上传成功", imageUrl);
            
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量上传宠物图片", description = "一次上传多张图片")
    @PostMapping("/pet-images")
    public Result uploadPetImages(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return Result.error("请选择要上传的图片");
        }
        
        if (files.length > 10) {
            return Result.error("一次最多上传10张图片");
        }

        java.util.List<String> imageUrls = new java.util.ArrayList<>();
        
        for (MultipartFile file : files) {
            Result result = uploadPetImage(file);
            if (result.getCode() != 200) {
                return result; // 有一张失败就返回错误
            }
            imageUrls.add((String) result.getData());
        }
        
        return Result.success("上传成功", imageUrls);
    }
}
