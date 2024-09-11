package com.sparta3tm.companyserver.application.dtos.gemini;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiReqDto {

    private List<Content> contents;

    @Getter
    @Setter
    public class Content{

        private List<Part> parts;

        public Content(String text){
            parts = new ArrayList<>();
            Part part = new Part(text);
            parts.add(part);
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public class Part{
            private String text;
        }
    }

    public void setGeminiReqDto(String text){
        this.contents = new ArrayList<>();
        Content content = new Content(text);
        contents.add(content);
    }
}
