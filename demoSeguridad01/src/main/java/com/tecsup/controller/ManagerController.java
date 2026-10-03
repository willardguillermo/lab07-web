package com.tecsup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/reportes")
    public String reportes() {
        return "Bienvenido MANAGER - Reportes";
    }
}
