package com.codehows.wqproject.repository;

import com.codehows.wqproject.entity.Image;
import com.codehows.wqproject.entity.Lecture;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Long> {
    Optional<Image> findByLecture(Lecture lecture);
}
