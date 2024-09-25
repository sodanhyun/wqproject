package com.codehows.wqproject.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Slf4j
public class FileHandler {

    public String uploadFile(String uploadPath, MultipartFile file) throws RuntimeException, IOException {
        String uuid = UUID.randomUUID().toString();
        File destDir = new File(uploadPath);
        if(!destDir.exists()) {
            if(!destDir.mkdirs()) {
                log.info("이미지 저장 디렉터리 생성 실패");
                throw new RuntimeException();
            }
        }
        String saveFileName = uuid + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadPath + File.separator + saveFileName);
        file.transferTo(savePath);
        return saveFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("기존 파일 삭제됨");
        }
    }

}
