package com.sparta3tm.hubserver.application.dto.ai;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {

    private List<Content> contents;

    @Data
    public static class Content {

        private Part parts;

    }

    @Data
    public static class Part {

        private String text;

    }

    public ChatRequest(String text) {
        this.contents = new ArrayList<>();
        Content content = new Content();
        Part parts = new Part();

        parts.setText(text);
        content.setParts(parts);
        this.contents.add(content);
    }

}