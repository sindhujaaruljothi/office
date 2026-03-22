package com.dundermifflin.api.controller;

import com.dundermifflin.api.controller.support.SkeletonResponseFactory;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping
    public ResponseEntity<Map<String, String>> findAll() {
        return SkeletonResponseFactory.notImplemented("customers");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> findById(@PathVariable Integer id) {
        return SkeletonResponseFactory.notImplemented("customers/" + id);
    }
}
