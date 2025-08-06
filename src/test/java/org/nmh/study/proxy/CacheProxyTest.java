package org.nmh.study.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nmh.study.model.User;
import org.nmh.study.service.UserService;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheProxyTest {

    @Mock
    private UserService mockUserService;
    
    private UserService proxyUserService;

    @BeforeEach
    void setUp() {
        proxyUserService = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class<?>[]{UserService.class},
                new CacheProxy(mockUserService)
        );
    }

    @Test
    void 프록시는_실제_서비스를_호출한다() {
        // given
        Long userId = 1L;
        User expectedUser = new User(userId, "나민혁", 26);
        when(mockUserService.getById(userId)).thenReturn(expectedUser);

        // when
        User actualUser = proxyUserService.getById(userId);

        // then
        assertThat(actualUser).isEqualTo(expectedUser);
        verify(mockUserService, times(1)).getById(userId);
    }

    @Test
    void 캐시가_동작하여_두번째_호출시_실제_서비스를_호출하지_않는다() {
        // given
        Long userId = 1L;
        User expectedUser = new User(userId, "나민혁", 26);
        when(mockUserService.getById(userId)).thenReturn(expectedUser);

        // when
        User firstCall = proxyUserService.getById(userId);
        User secondCall = proxyUserService.getById(userId);

        // then
        assertThat(firstCall).isEqualTo(expectedUser);
        assertThat(secondCall).isEqualTo(expectedUser);
        verify(mockUserService, times(1)).getById(userId); // 한 번만 호출되어야 함
    }

    @Test
    void getById가_아닌_다른_메서드는_null을_반환한다() {
        // when
        proxyUserService.deleteById(1L);

        // then - CacheProxy.invoke()에서 getById가 아니면 null을 반환하므로 실제 서비스 호출이 안됨
        verifyNoInteractions(mockUserService);
    }
}