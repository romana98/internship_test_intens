package com.project.internship.helper;

import java.util.List;

public interface MapperInterface<T, U> {

    T toEntity(U dto);

    U toDto(T entity);

    List<U> toDTOList(List<T> entities);
}
