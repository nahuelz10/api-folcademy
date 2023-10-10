package com.folcademy.exampleapi.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saludo")
public class SaludoController {

    @GetMapping("/hello")
    public String hello(){
        return "Hola mundo!";
    }

    @PostMapping("/goodbye")
    public String goodbye(){
        return "Adios, hasta luego!";
    }

    @PutMapping("/nombre")
    public String nombre(){
        return "Mi nombre es Nahuel";
    }
}
