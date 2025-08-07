package org.nmh.study.controller;

import org.nmh.study.model.User;
import org.nmh.study.proxy.CacheProxy;
import org.nmh.study.service.UserService;
import org.nmh.study.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Proxy;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = (UserService) Proxy.newProxyInstance(
            UserService.class.getClassLoader(),
            new Class<?>[]{UserService.class},
            new CacheProxy(userService)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
