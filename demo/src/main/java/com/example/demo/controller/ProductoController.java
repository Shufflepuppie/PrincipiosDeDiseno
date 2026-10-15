package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;   // ← nuevo import



import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:63342")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping("/sin-cache")
    public Map<String, Object> sinCache() {
        long inicio = System.nanoTime();
        List<Producto> productos = service.listarSinCache();
        double ms = (System.nanoTime() - inicio) / 1_000_000.0;

        return Map.of("modo", "sin cache", "registros", productos.size(), "tiempoMs", ms);
    }

    @GetMapping("/con-cache")
    public Map<String, Object> conCache() {
        long inicio = System.nanoTime();
        List<Producto> productos = service.listarConCache();
        double ms = (System.nanoTime() - inicio) / 1_000_000.0;

        return Map.of("modo", "con cache", "registros", productos.size(), "tiempoMs", ms);
    }

    @GetMapping("/limpiar-cache")
    public Map<String, Object> limpiarCache() {
        service.limpiarCache();
        return Map.of("mensaje", "Cache vaciada");
    }
}