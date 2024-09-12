package com.sparta3tm.choreserver.application.dtos.slack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlackIncomingHookDto {
    private String channel;
    private String text;
}
