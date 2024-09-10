package com.sparta3tm.hubserver.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta3tm.hubserver.application.dto.ai.ChatRequest;
import com.sparta3tm.hubserver.application.dto.ai.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;
    @Value("${gemini.api.url}")
    private String geminiUrl;
    private final RestTemplate restTemplate;
    private final HubService hubService;


    public String requestAi(Long startHubId, Long endHubId) throws JsonProcessingException {
        String requestUrl = geminiUrl + "?key=" + geminiApiKey;
        ChatRequest chatRequest = new ChatRequest(createPrompt(startHubId, endHubId));
        ChatResponse response = restTemplate.postForObject(requestUrl, chatRequest, ChatResponse.class);

        String text = response.getCandidates().get(0).getContent().getParts().get(0).getText();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(text);

        List<String> root = new ArrayList<>();
        int number = jsonNode.get("number").asInt();
        for (JsonNode node : jsonNode.get("root")) {
            root.add(node.asText());
        }

        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    private String createPrompt(Long startHubId, Long endHubId) {
        return "현재 존재하는 Hub 들은" + hubService.allHub() + " 다음과 같다.\n" +
                "위 Hub 데이터들의 위치 정보를 기반으로 출발 허브와 목적지 허브간의 경유 가능한 허브를 찾으려고 한다.\n" +
                "출발 허브를 " + hubService.searchHubById(startHubId) + " 이고 " +
                "목적지 허브를 " + hubService.searchHubById(endHubId) + " 라고 할 때, " +
                "경유 가능한 Hub 들을 현재 존재하는 Hub 들 중에서 선택하여 나에게 알려줘.\n" +
                "답변은 꼭 아래에서 제시한 형식으로만 대답하고 다른 부가 설명은 절대로 해서는 안돼.\n" +
                "지도에서 볼 때, 출발지와 목적지의 경로 상에 있는 Hub 들 중 경유해서 갈 수 있을 만하기에 적합한 허브들을 넣어줘.\n" +
                "지도에서 볼 때, 위치 상으로 조금이라도 경유하면 좋을 것 같은 경우가 있다고 생각된다면 경유 가능한 Hub 로 즉시 추가해줘.\n" +
                "위 조건들을 토대로 경유할 Hub 가 존재하지 않을 경우가 생긴다면 number = 0, root = []를 반환해줘.\n" +
                "경유 가능한 허브들의 수의 범위는 0 ~ 10개로 고려해줘.\n" +
                "예를 들어 \n" +
                "출발 허브의 Id 가 1인 서울특별시 센터에서 목적지 허브의 Id 가 4인 부산광역시 센터로 갈 경우,\n" +
                "지도 상에서 봤을 때, 경유 가능한 허브들은 id 가 2인 경기 북부센터와 id 가 3인 경기 남부센터는 경유 가능한 Hub 들로 볼 수 있어.\n" +
                "그렇게 된다면 number = 2, root = 경기 북부센터와 경기 남부센터가 들어가겠지. 내가 설명한 방식대로 경유 가능한 Hub 들을 Json 형식으로 반환해줘.\n" +
                "{\n" +
                "   \"number\": { 경유 가능한 허브들의 수 }\n" +
                "   \"root\": [\"경유되는 첫번째 HubName\", \"경유되는 두번째 HubName\", ... , \"경유되는 마지막 HubName\"]\n" +
                "}\n" +
                "참고로 앞뒤로 ```json, ```)] 이런 부분도 전부 생략해서 Json 만 반환해줘.";
    }
}
