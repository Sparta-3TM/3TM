package com.sparta3tm.choreserver.application.dtos.slack;

import com.sparta3tm.choreserver.domain.slack.Slack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessageResDto {
    Long id;
    String receiverId;
    String message;
    LocalDateTime sendTime;

    public static SlackMessageResDto from (Slack slack){
        return SlackMessageResDto.builder()
                .id(slack.getId())
                .receiverId(slack.getReceiverId())
                .message(slack.getMessage())
                .sendTime(slack.getSendTime())
                .build();
    }
}
