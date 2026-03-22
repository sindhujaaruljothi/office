package com.dundermifflin.api.common.controller;

import com.dundermifflin.api.common.service.ReadOnlyService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class AbstractReadOnlyController<T, ID> {

    private final ReadOnlyService<T, ID> readOnlyService;

    protected AbstractReadOnlyController(ReadOnlyService<T, ID> readOnlyService) {
        this.readOnlyService = readOnlyService;
    }

    @GetMapping
    public List<T> findAll() {
        return readOnlyService.findAll();
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable ID id) {
        return readOnlyService.findById(id);
    }
}
