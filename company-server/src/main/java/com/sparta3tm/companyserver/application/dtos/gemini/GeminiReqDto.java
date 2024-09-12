package com.sparta3tm.companyserver.application.dtos.gemini;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeminiReqDto {

    private List<Content> contents;

    @Data
    public class Content{

        private List<Part> parts;

        public Content(String text){
            parts = new ArrayList<>();
            Part part = new Part(text);
            parts.add(part);
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class Part{
            private String text;
        }
    }

    public void createGeminiReqDto(String text){
        this.contents = new ArrayList<>();
        Content content = new Content(text);
        contents.add(content);
    }
}
