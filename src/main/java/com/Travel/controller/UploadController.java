package com.Travel.controller;


import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Api
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private MinioClient minioClient;
    @PostMapping("/add")
    public void upload(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String mimeType = getMimeType(extension);
        try {
            File tempFile = File.createTempFile("img", "temp");
            file.transferTo(tempFile);
            String absolutePath = tempFile.getAbsolutePath();
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("travel")
//                    .object("test001.mp4")
                    .filename(absolutePath)
                    .contentType(mimeType)
                    .object(fileName)//添加子目录
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws IOException {
        // 将文件保存到临时目录中
        File tempFile = File.createTempFile("temp-", ".jpg");
        Files.write(tempFile.toPath(), file.getBytes());
        // 生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + ".jpg";

        // 保存文件路径和文件名的关联关系
        Map<String, String> filePaths = new HashMap<>();
        filePaths.put(path, fileName);

        // 返回成功响应或其他处理逻辑
        return "File uploaded successfully!";
    }

    @PostMapping("/save")
    public String saveFile(@RequestParam("path") String path) throws IOException {
        // 获取临时目录中的文件数据
        Map<String, String> filePaths = getFilePaths(); // 获取文件路径和文件名的关联关系
        String fileName = filePaths.get(path);
        File tempFile = new File("temp/" + fileName);
        byte[] fileData = Files.readAllBytes(tempFile.toPath());

        // 保存文件到Minio
        String objectName = "uploads/" + fileName;
        long objectSize = fileData.length;
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket("mybucket")
                    .object(objectName)
                    .stream(new ByteArrayInputStream(fileData), objectSize, -1)
                    .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }

        // 删除临时文件
        tempFile.delete();

        return "File saved successfully!";
    }

    // 用于保存文件路径和文件名的关联关系，可以根据实际需求使用数据库或其他持久化方式
    private Map<String, String> getFilePaths() {
        // 返回之前保存的文件路径和文件名的关联关系
        return null;
    }

    private String getMimeType(String extension){
        if(extension==null)
            extension = "";
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        //通用mimeType，字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }
}
