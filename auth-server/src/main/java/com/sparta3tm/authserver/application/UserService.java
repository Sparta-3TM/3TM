package com.sparta3tm.authserver.application;

import com.sparta3tm.authserver.application.dtos.user.UserResDto;
import com.sparta3tm.authserver.application.dtos.user.UserUpdateReqDto;
import com.sparta3tm.authserver.domain.user.User;
import com.sparta3tm.authserver.domain.user.UserRepository;
import com.sparta3tm.common.support.error.CoreApiException;
import com.sparta3tm.common.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "UserService")
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResDto getUser(Long userId) {
        return UserResDto.from(userRepository.findById(userId).orElseThrow(()->{
            log.error("존재하지 않는 User ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }));
    }

    public UserResDto getUserByUserId(String userId) {
        return UserResDto.from(userRepository.findByUserId(userId).orElseThrow(()->{
            log.error("존재하지 않는 User ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        }));
    }

    public List<UserResDto> searchUser(String keyword, Pageable pageable) {
        try{
            if(keyword == null){
                return userRepository.findAllByUserNameContaining("", pageable).stream().map(UserResDto::from).toList();
            }
            return userRepository.findAllByUserNameContaining(keyword, pageable).stream().map(UserResDto::from).toList();
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }


    public UserResDto updateUser(Long userId, UserUpdateReqDto userUpdateReqDto) {
        User user = userRepository.findById(userId).orElseThrow(()->{
            log.error("존재하지 않는 User ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        try{
            user.updateUserInfo(userUpdateReqDto.getUsername(),
                    userUpdateReqDto.getPhoneNumber(), userUpdateReqDto.getRole());

            User savedUser = userRepository.save(user);

            return UserResDto.from(savedUser);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }
    }

    public String deleteUser(Long userId, String headUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("존재하지 않는 User ID 입니다.");
            throw new CoreApiException(ErrorType.NOT_FOUND_ERROR);
        });

        try{
            user.softDelete(headUserId);
            userRepository.save(user);
        }catch (Exception e){
            log.error("INTERNAL SERVER ERROR");
            throw new CoreApiException(ErrorType.DEFAULT_ERROR);
        }

        return "User " + user.getId() + " is Deleted";
    }


}
