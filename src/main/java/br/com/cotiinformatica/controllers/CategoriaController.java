package br.com.cotiinformatica.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cotiinformatica.dtos.CategoriaGetDto;
import br.com.cotiinformatica.entities.Categoria;
import br.com.cotiinformatica.repositories.CategoriaRepository;

@RestController
@RequestMapping(value = "/api/categorias")
public class CategoriaController {
	@Autowired // auto inicialização
	CategoriaRepository categoriaRepository;

	@Autowired
	ModelMapper modelMapper;
	
	@GetMapping()
	public List<CategoriaGetDto> get() {


		// fazendo a consulta de categorias
		List<Categoria> categorias = categoriaRepository.findAll();

		// copiando as categorias obtidas do banco para a lista abaixo
		List<CategoriaGetDto> result = modelMapper.map(categorias, new TypeToken<List<CategoriaGetDto>>() {
		}.getType());

		// retornando os dados
		return result;
	}
}
