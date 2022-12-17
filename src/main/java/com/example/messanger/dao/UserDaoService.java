package com.example.messanger.dao;

import com.example.messanger.dao.repository.UserInfoRepository;
import com.example.messanger.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDaoService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;



}
