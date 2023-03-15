package com.gestion.empleados.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.empleados.entidades.Libros;
import com.gestion.empleados.repositorios.LibroRepository;

@Service
public class LibroServiceImpl implements LibroService {

	@Autowired
	private LibroRepository empleadoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Libros> findAll() {
		return (List<Libros>) empleadoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Libros> findAll(Pageable pageable) {
		return empleadoRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Libros empleado) {
		empleadoRepository.save(empleado);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		empleadoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Libros findOne(Long id) {
		return empleadoRepository.findById(id).orElse(null);
	}
	
}
