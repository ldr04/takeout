package com.myvamp.vamp.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myvamp.vamp.entity.User;
import com.myvamp.vamp.mapper.UserMapper;
import com.myvamp.vamp.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
