package com.sparta3tm.hubserver.application.service;

import com.sparta3tm.common.support.RoleCheck;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import com.sparta3tm.hubserver.application.dto.hub.request.RequestHubDto;
import com.sparta3tm.hubserver.application.dto.hub.response.ResponseHubDto;
import com.sparta3tm.hubserver.application.dto.hub.response.ResponsePageHubDto;
import com.sparta3tm.hubserver.domain.entity.Hub;
import com.sparta3tm.hubserver.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final RoleCheck roleCheck = new RoleCheck();

    @Transactional
    @CacheEvict(cacheNames = "hub_list_cache", allEntries = true)
    public ResponseHubDto createHub(RequestHubDto requestHubDto) {
        return ResponseHubDto.of(hubRepository.save(new Hub(requestHubDto)));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "hub_cache", key = "args[0]")
    public ResponseHubDto searchHubById(Long hubId) {
        return ResponseHubDto.of(hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR)));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "hub_list_cache", key = "getMethodName()")
    public ResponsePageHubDto searchHubList(Pageable pageable) {
        Page<Hub> page = hubRepository.findAllByIsDeleteFalse(pageable);
        return ResponsePageHubDto.of(page.stream().map(ResponseHubDto::of).toList(), page.hasNext());
    }

    @Transactional
    @CachePut(cacheNames = "hub_cache", key = "args[0]")
    @CacheEvict(cacheNames = "hub_list_cache", allEntries = true)
    public ResponseHubDto updateHub(Long hubId, RequestHubDto requestHubDto) {
        Hub hub = hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        return ResponseHubDto.of(hub.update(requestHubDto));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "hub_cache", key = "args[0]"),
            @CacheEvict(cacheNames = "hub_list_cache", allEntries = true)
    })
    public void deleteHub(Long hubId, String userId) {
        Hub hub = hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        hub.softDelete(userId);
    }

    public List<Hub> searchHubByIdIn(List<Long> hubIdList) {
        return hubRepository.findAllByIdIn(hubIdList);
    }



    public List<ResponseHubDto> allHub() {
        return hubRepository.findAllByIsDeleteFalse().stream().map(ResponseHubDto::of).toList();
    }

    public ResponseHubDto updateManager(Long hubId, String userId, String userRole) {
        if (roleCheck.CHECK_SHIPPER(userRole) || roleCheck.CHECK_COMPANY(userRole))
            throw new CoreApiException(ErrorType.BAD_REQUEST);
        Hub hub = hubRepository.findByIdAndIsDeleteFalse(hubId).orElseThrow(() -> new CoreApiException(ErrorType.NOT_FOUND_ERROR));
        return ResponseHubDto.of(hubRepository.save(hub.updateManager(userId)));
        }
}
