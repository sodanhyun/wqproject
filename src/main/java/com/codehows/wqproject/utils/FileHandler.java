package com.codehows.wqproject.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Slf4j
public class FileHandler {

    public static final String THUMBNAIL_IMAGE_SUFFIX = "thumbs_";

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
        //썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
        Thumbnailator.createThumbnail(
                new File(uploadPath + File.separator + saveFileName),
                new File(uploadPath + File.separator + THUMBNAIL_IMAGE_SUFFIX + saveFileName),150, 150);

        return saveFileName;
    }

    public Resource loadFileAsResource(String dirPath, Boolean isThumb, String fileName) throws FileNotFoundException {
        String filePath = dirPath + (isThumb ? THUMBNAIL_IMAGE_SUFFIX : "") + fileName;
        File file = new File(filePath);
        if(!file.exists()) {
            log.info("[{}] 파일이 존재하지 않습니다.", file.getPath());
            throw new FileNotFoundException(file.getPath());
        }
        return new FileSystemResource(filePath);
    }

    public void deleteFile(String filePath) {
        File originFile = new File(filePath);
        if(originFile.exists() && originFile.delete()) {
            log.info("[{}] 파일이 삭제되었습니다.", originFile.getPath());
        }
        File encodedFile = new File(filePath + THUMBNAIL_IMAGE_SUFFIX);
        if(encodedFile.exists() && encodedFile.delete()) {
            log.info("[{}] 파일이 삭제되었습니다.", encodedFile.getPath());
        }
    }

}
