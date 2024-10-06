package com.codehows.wqproject.domain.lecture.requestDto;

import com.codehows.wqproject.entity.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ImageReq {

    private Long id;

    private String oriImgName;

    private String imgUrl;

    private String regTime;
    private String updateTime;

    public static ModelMapper modelMapper = new ModelMapper();

    public ImageReq(String oriImgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }

    public static ImageReq of(Image image) {
        return modelMapper.map(image, ImageReq.class);
    }

}
