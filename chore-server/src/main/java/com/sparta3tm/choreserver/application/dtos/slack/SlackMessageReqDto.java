package com.sparta3tm.choreserver.application.dtos.slack;

import com.sparta3tm.choreserver.domain.slack.Slack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageReqDto {
    String receiverId;
    String message;

    public Slack toEntity(LocalDateTime sendTime){
        return Slack.builder()
                .receiverId(this.receiverId)
                .message(this.message)
                .sendTime(sendTime)
                .build();
    }
}
