package org.nmh.study.service;

import org.nmh.study.model.User;

public interface UserService {
    User getById(Long id);
    void deleteById(Long id);
}
