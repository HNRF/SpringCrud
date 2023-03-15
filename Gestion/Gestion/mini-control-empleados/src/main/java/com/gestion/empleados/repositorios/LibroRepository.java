package com.gestion.empleados.repositorios;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.gestion.empleados.entidades.Libros;

public interface LibroRepository extends PagingAndSortingRepository<Libros, Long>{

}
