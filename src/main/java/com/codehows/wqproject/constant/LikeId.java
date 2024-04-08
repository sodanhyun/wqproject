package com.codehows.wqproject.constant;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LikeId implements Serializable {

    private String qCode;

    private String email;


}
