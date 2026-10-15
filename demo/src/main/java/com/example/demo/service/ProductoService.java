package com.example.demo.service;

import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    // SIN caché: siempre va a la base de datos
    public List<Producto> listarSinCache() {
        return repo.findAll();
    }

    // CON caché: la primera vez va a la BD, después devuelve lo guardado en memoria
    @Cacheable("productos")
    public List<Producto> listarConCache() {
        return repo.findAll();
    }

    // Vacía la caché, para repetir el experimento sin reiniciar la app
    @CacheEvict(value = "productos", allEntries = true)
    public void limpiarCache() {
    }
}