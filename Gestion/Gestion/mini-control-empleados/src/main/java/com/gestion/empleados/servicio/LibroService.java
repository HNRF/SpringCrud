package com.gestion.empleados.servicio;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gestion.empleados.entidades.Libros;

public interface LibroService {

	public List<Libros> findAll();

	public Page<Libros> findAll(Pageable pageable);

	public void save(Libros libros);

	public Libros findOne(Long id);

	public void delete(Long id);
}
