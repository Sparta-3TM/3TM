package com.sparta3tm.hubserver.hub.controller;

import com.sparta3tm.hubserver.application.dto.hub.RequestHubDto;
import com.sparta3tm.hubserver.application.dto.hub.ResponseHubDto;
import com.sparta3tm.hubserver.application.dto.hub.ResponsePageHubDto;
import com.sparta3tm.hubserver.application.service.HubService;
import com.sparta3tm.hubserver.hub.RestDocsTest;
import com.sparta3tm.hubserver.presentation.controller.HubController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HubControllerTest extends RestDocsTest {

    private final HubService hubService = mock(HubService.class);

    @Override
    protected Object initializeController() {
        return new HubController(hubService);
    }

    @Test
    @DisplayName("허브 생성 API")
    public void CREAT_HUB() throws Exception {
        RequestHubDto requestHubDto = new RequestHubDto("hubName", "address", 36.5, 37.5);
        given(hubService.createHub(any()))
                .willReturn(new ResponseHubDto(1L, requestHubDto.hubName(), requestHubDto.address(), requestHubDto.latitude(), requestHubDto.longitude()));

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/hubs")
                        .header("Authorization", "Bearer {AccessToken}")
                        .content(objectMapper.writeValueAsString(requestHubDto))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("create-hub",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestHeaders(headerWithName("Authorization").description("AccessToken")
                                ),
                                requestFields(
                                        fieldWithPath("hubName").type(STRING).description("허브 이름"),
                                        fieldWithPath("address").type(STRING).description("허브 주소"),
                                        fieldWithPath("latitude").type(NUMBER).description("허브 위도"),
                                        fieldWithPath("longitude").type(NUMBER).description("허브 경도")
                                ),
                                responseFields(
                                        fieldWithPath("result").type(STRING).description("결과 타입"),
                                        fieldWithPath("data").type(OBJECT).description("결과 데이터 hub"),
                                        fieldWithPath("data.id").type(NUMBER).description("hub id"),
                                        fieldWithPath("data.hubName").type(STRING).description("hub name"),
                                        fieldWithPath("data.address").type(STRING).description("hub address"),
                                        fieldWithPath("data.latitude").type(NUMBER).description("hub latitude"),
                                        fieldWithPath("data.longitude").type(NUMBER).description("hub longitude"),
                                        fieldWithPath("error").type(OBJECT).optional().description("오류 정보 ( 결과 타입이 Success 일 경우, null )"))
                        )
                );
    }

    @Test
    @DisplayName("허브 단건 조회 API")
    public void SEARCH_HUB_BY_ID() throws Exception {
        given(hubService.searchHubById(any()))
                .willReturn(new ResponseHubDto(1L, "hubName", "address", 36.5, 37.5));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hubs/{hubId}", 1L)
                        .header("Authorization", "Bearer {AccessToken}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("search-hub-by-id",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("hubId").description("허브 ID")),
                                responseFields(
                                        fieldWithPath("result").type(STRING).description("결과 타입"),
                                        fieldWithPath("data").type(OBJECT).description("결과 데이터 hub"),
                                        fieldWithPath("data.id").type(NUMBER).description("hub id"),
                                        fieldWithPath("data.hubName").type(STRING).description("hub name"),
                                        fieldWithPath("data.address").type(STRING).description("hub address"),
                                        fieldWithPath("data.latitude").type(NUMBER).description("hub latitude"),
                                        fieldWithPath("data.longitude").type(NUMBER).description("hub longitude"),
                                        fieldWithPath("error").type(OBJECT).optional().description("오류 정보 ( 결과 타입이 Success 일 경우, null )")
                                )
                        )
                );
    }

    @Test
    @DisplayName("허브 페이징 조회 API")
    public void SEARCH_HUB_LIST() throws Exception {
        given(hubService.searchHubList(any()))
                .willReturn(new ResponsePageHubDto(
                        List.of(new ResponseHubDto(1L, "hubName", "address", 36.5, 37.5)),
                        false
                ));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/hubs")
                        .header("Authorization", "Bearer {AccessToken}")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt")
                        .param("direction", "DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("search-hub-list",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("page").description("페이지 번호"),
                                        parameterWithName("size").description("페이지 당 데이터 수"),
                                        parameterWithName("sort").description("정렬 기준 필드"),
                                        parameterWithName("direction").description("정렬 방향")),
                                responseFields(
                                        fieldWithPath("result").type(STRING).description("결과 타입"),
                                        fieldWithPath("data").type(OBJECT).description("결과 데이터 hub"),
                                        fieldWithPath("data.responseHubDtoList").type(ARRAY).description("hub list"),
                                        fieldWithPath("data.responseHubDtoList[].id").type(NUMBER).description("허브 ID"),
                                        fieldWithPath("data.responseHubDtoList[].hubName").type(STRING).description("허브 이름"),
                                        fieldWithPath("data.responseHubDtoList[].address").type(STRING).description("주소"),
                                        fieldWithPath("data.responseHubDtoList[].latitude").type(NUMBER).description("위도"),
                                        fieldWithPath("data.responseHubDtoList[].longitude").type(NUMBER).description("경도"),
                                        fieldWithPath("data.hasNext").type(BOOLEAN).description("다음 페이지 유무"),
                                        fieldWithPath("error").type(OBJECT).optional().description("오류 정보 ( 결과 타입이 Success 일 경우, null )")
                                )
                        )
                );
    }

    @Test
    @DisplayName("허브 업데이트 API")
    public void UPDATE_HUB() throws Exception {
        RequestHubDto requestHubDto = new RequestHubDto("hubName", "address", 36.5, 37.5);
        given(hubService.updateHub(any(), any()))
                .willReturn(new ResponseHubDto(1L, requestHubDto.hubName(), requestHubDto.address(), requestHubDto.latitude(), requestHubDto.longitude()));

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/hubs/{hubId}", 1L)
                        .header("Authorization", "Bearer {AccessToken}")
                        .content(objectMapper.writeValueAsString(requestHubDto))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-hub",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("hubId").description("허브 ID")),
                                requestHeaders(headerWithName("Authorization").description("AccessToken")),
                                requestFields(
                                        fieldWithPath("hubName").type(STRING).description("허브 이름"),
                                        fieldWithPath("address").type(STRING).description("허브 주소"),
                                        fieldWithPath("latitude").type(NUMBER).description("허브 위도"),
                                        fieldWithPath("longitude").type(NUMBER).description("허브 경도")),
                                responseFields(
                                        fieldWithPath("result").type(STRING).description("결과 타입"),
                                        fieldWithPath("data").type(OBJECT).description("결과 데이터 hub"),
                                        fieldWithPath("data.id").type(NUMBER).description("hub id"),
                                        fieldWithPath("data.hubName").type(STRING).description("hub name"),
                                        fieldWithPath("data.address").type(STRING).description("hub address"),
                                        fieldWithPath("data.latitude").type(NUMBER).description("hub latitude"),
                                        fieldWithPath("data.longitude").type(NUMBER).description("hub longitude"),
                                        fieldWithPath("error").type(OBJECT).optional().description("오류 정보 ( 결과 타입이 Success 일 경우, null )")
                                )
                        )
                );
    }

    @Test
    @DisplayName("허브 삭제 API")
    public void DELETE_HUB() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/hubs/{hubId}", 1L)
                        .header("Authorization", "Bearer {AccessToken}")
                        .header("X-USER-NAME", "username"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-hub",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("hubId").description("허브 ID")),
                        requestHeaders(
                                headerWithName("Authorization").description("AccessToken"),
                                headerWithName("X-USER-NAME").description("username")
                        ),
                        responseFields(
                                fieldWithPath("result").type(STRING).description("결과 타입"),
                                fieldWithPath("data").type(OBJECT).optional().description("결과 데이터 hub ( 삭제 성공시 null"),
                                fieldWithPath("error").type(OBJECT).optional().description("오류 정보 ( 결과 타입이 Success 일 경우, null )")
                        )
                ));


    }

}
