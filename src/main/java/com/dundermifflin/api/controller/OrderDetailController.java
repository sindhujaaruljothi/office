package com.dundermifflin.api.controller;

import com.dundermifflin.api.controller.support.SkeletonResponseFactory;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderdetails")
public class OrderDetailController {

    @GetMapping
    public ResponseEntity<Map<String, String>> findAll() {
        return SkeletonResponseFactory.notImplemented("orderdetails");
    }
}
