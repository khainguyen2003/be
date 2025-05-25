package com.datn.motchill.shared.services;

import com.datn.motchill.shared.dto.ResponseProcedure;

import java.util.List;
import java.util.Map;

public interface ExecutorService {

    <T> ResponseProcedure<T> execute(String procedureName, Map<String, Object> inParams, Class<T> dtoClass);

    <T> List<T> executeNativeQuery(String sql, List<Object> params, Class<T> dtoClass);
}
