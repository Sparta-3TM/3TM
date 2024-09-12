package com.sparta3tm.choreserver.domain.slack;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "p_slacks")
public class Slack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String receiverId;

    @Column(length = 1000)
    String message;

    LocalDateTime sendTime;
}
