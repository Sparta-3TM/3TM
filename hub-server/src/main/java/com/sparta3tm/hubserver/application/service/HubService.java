package com.sparta3tm.hubserver.application.service;

import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.hubserver.application.dto.RequestHubDto;
import com.sparta3tm.hubserver.application.dto.ResponseHubDto;
import com.sparta3tm.hubserver.application.dto.ResponsePageHubDto;
import com.sparta3tm.hubserver.domain.entity.Hub;
import com.sparta3tm.hubserver.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    @Transactional
    public ResponseHubDto createHub(RequestHubDto requestHubDto) {
        return ResponseHubDto.of(hubRepository.save(new Hub(requestHubDto)));
    }

    @Transactional(readOnly = true)
    public ResponseHubDto searchHubById(Long hubId) {
        return ResponseHubDto.of(hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR)));
    }

    @Transactional(readOnly = true)
    public ResponsePageHubDto searchHubList(Pageable pageable) {
        Page<Hub> page = hubRepository.findAllByIsDeleteFalse(pageable);
        return ResponsePageHubDto.of(page.stream().map(ResponseHubDto::of).toList(), page.hasNext());
    }

    @Transactional
    public ResponseHubDto updateHub(Long hubId, RequestHubDto requestHubDto) {
        Hub hub = hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        return ResponseHubDto.of(hub.update(requestHubDto));
    }

    @Transactional
    public ResponseHubDto deleteHub(Long hubId, String username) {
        Hub hub = hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        hub.softDelete(username);
        return ResponseHubDto.of(hub);
    }
}
