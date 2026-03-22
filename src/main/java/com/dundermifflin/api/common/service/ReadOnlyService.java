package com.dundermifflin.api.common.service;

import java.util.List;

public interface ReadOnlyService<T, ID> {

    List<T> findAll();

    T findById(ID id);
}
