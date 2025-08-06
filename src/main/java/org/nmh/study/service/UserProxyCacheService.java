package org.nmh.study.service;

import org.nmh.study.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserProxyCacheService implements UserService {

    private final UserService userService;

    private ConcurrentHashMap<Long, User> cache = new ConcurrentHashMap<>();

    public UserProxyCacheService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public User getById(Long id) {
        if(cache.containsKey(id)) {
            return cache.get(id);
        }
        return userService.getById(id);
    }
}
