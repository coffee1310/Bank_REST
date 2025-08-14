package com.example.bank_rest.util.converter;

import com.example.bank_rest.exception.UserDoesNotExistException;

public interface EntityConverter<T1, T2> {
    T1 toEntity(T2 dto) throws UserDoesNotExistException;
    T2 toDto(T1 entity) throws UserDoesNotExistException;
}
