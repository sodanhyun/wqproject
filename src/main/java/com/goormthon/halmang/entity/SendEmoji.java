package com.goormthon.halmang.entity;

import com.goormthon.halmang.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "send_emoji")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SendEmoji extends BaseTimeEntity {

    @Id
    @Column(name = "send_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sendSeq;

    @Column(name = "e_id")
    private String eId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "receiver_id", nullable = false)
    private User receiver;

    private Boolean readFlag = false;

}
