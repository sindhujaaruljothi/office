package com.dundermifflin.api.controller;

import com.dundermifflin.api.controller.support.SkeletonResponseFactory;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regions")
public class RegionController {

    @GetMapping
    public ResponseEntity<Map<String, String>> findAll() {
        return SkeletonResponseFactory.notImplemented("regions");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> findById(@PathVariable Integer id) {
        return SkeletonResponseFactory.notImplemented("regions/" + id);
    }
}
