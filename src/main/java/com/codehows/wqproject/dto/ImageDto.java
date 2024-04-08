package com.codehows.wqproject.dto;

import com.codehows.wqproject.entity.Image;
import com.codehows.wqproject.entity.Lecture;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@ToString
public class ImageDto {

    private Long id;

    private String oriImgName;

    private String imgUrl;

    private String regTime;
    private String updateTime;

    public static ModelMapper modelMapper = new ModelMapper();

    public ImageDto(String oriImgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }

    public static ImageDto of(Image image) {
        return modelMapper.map(image, ImageDto.class);
    }

}
