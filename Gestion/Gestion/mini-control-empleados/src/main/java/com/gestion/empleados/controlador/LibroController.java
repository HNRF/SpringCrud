package com.gestion.empleados.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.gestion.empleados.servicio.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestion.empleados.entidades.Libros;
import com.gestion.empleados.util.paginacion.PageRender;
import com.gestion.empleados.util.reportes.ExporterExcel;
import com.gestion.empleados.util.reportes.ExporterPDF;
import com.lowagie.text.DocumentException;

@Controller
public class LibroController {

	@Autowired
	private LibroService libroService;

	@GetMapping("/ver/{id}")
	public String verDetallesDelEmpleado(@PathVariable(value = "id") Long id,Map<String,Object> modelo,RedirectAttributes flash) {
		Libros libro = libroService.findOne(id);
		if(libro == null) {
			flash.addFlashAttribute("error", "El libro no existe en la base de datos");
			return "redirect:/listar";
		}

		modelo.put("libro",libro);
		modelo.put("titulo", "Detalles del libro " + libro.getTitulo());
		return "ver";
	}

	@GetMapping({"/","/listar",""})
	public String listaLibros(@RequestParam(name = "page",defaultValue = "0") int page,Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Libros> libros = libroService.findAll(pageRequest);
		PageRender<Libros> pageRender = new PageRender<>("/listar", libros);

		modelo.addAttribute("titulo","Listado de libros");
		modelo.addAttribute("libros",libros);
		modelo.addAttribute("page", pageRender);

		return "listar";
	}

	@GetMapping("/form")
	public String registrarLibro(Map<String,Object> modelo) {
		Libros libro = new Libros();
		modelo.put("libro", libro);
		modelo.put("titulo", "Registro de libros");
		return "form";
	}

	@PostMapping("/form")
	public String guardarLibro(@Valid Libros libro, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			modelo.addAttribute("titulo", "Registro de libros");
			return "form";
		}

		String mensaje = (libro.getId() != null) ? "El libro ha sido editato con exito" : "libro registrado con exito";

		libroService.save(libro);
		status.setComplete();
		flash.addFlashAttribute("succe" + "ss", mensaje);
		return "redirect:/listar";
	}

	@GetMapping("/form/{id}")
	public String editarLibro(@PathVariable(value = "id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
		Libros libro = null;
		if(id > 0) {
			libro = libroService.findOne(id);
			if(libro == null) {
				flash.addFlashAttribute("error", "El ID del libro no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else {
			flash.addFlashAttribute("error", "El ID del libro no puede ser cero");
			return "redirect:/listar";
		}

		modelo.put("libro",libro);
		modelo.put("titulo", "Edición de libro");
		return "form";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarLibro(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			libroService.delete(id);
			flash.addFlashAttribute("success", "libro eliminado con exito");
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/exportarPDF")
	public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Empleados_" + fechaActual + ".pdf";
		
		response.setHeader(cabecera, valor);
		
		List<Libros> libro = libroService.findAll();
		
		ExporterPDF exporter = new ExporterPDF(libro);
		exporter.exportar(response);
	}
	
	@GetMapping("/exportarExcel")
	public void exportarListadoDeEmpleadosEnExcel(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/octet-stream");
		
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String fechaActual = dateFormatter.format(new Date());
		
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Empleados_" + fechaActual + ".xlsx";
		
		response.setHeader(cabecera, valor);
		
		List<Libros> libro = libroService.findAll();
		
		ExporterExcel exporter = new ExporterExcel(libro);
		exporter.exportar(response);
	}
}
