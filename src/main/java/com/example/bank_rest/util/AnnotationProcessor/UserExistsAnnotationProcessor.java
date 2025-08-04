package com.example.bank_rest.util.AnnotationProcessor;

import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.UserRepository;
import com.example.bank_rest.util.annotation.UserExists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanExpressionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class UserExistsAnnotationProcessor implements BeanPostProcessor {

    private final UserRepository userRepository;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();

        for (Field field: clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(UserExists.class)) {
                field.setAccessible(true);
                try {
                    Long userId = (Long) field.get(bean);

                    if (userId == null || !userRepository.existsById(userId)) {
                        throw new UserDoesNotExistException("User with ID " + userId + " does not exist");
                    }

                }
                catch (IllegalAccessException | UserDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
