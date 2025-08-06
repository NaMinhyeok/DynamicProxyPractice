package org.nmh.study.proxy;

import org.nmh.study.model.User;
import org.nmh.study.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheProxy implements InvocationHandler {

    private final UserService target;
    private final ConcurrentHashMap<Long, User> cache = new ConcurrentHashMap<>();

    public CacheProxy(UserService target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getById") && args.length == 1 && args[0] instanceof Long id) {
            if (cache.containsKey(id)) {
                return cache.get(id);
            } else {
                User user = (User) method.invoke(target, args);
                cache.put(id, user);
                return user;
            }
        }
        return null;
    }
}
