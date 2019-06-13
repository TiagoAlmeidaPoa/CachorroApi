package br.com.fundatec.ExemploApis.api.v1.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fundatec.ExemploApis.api.v1.dto.CachorroInputDto;
import br.com.fundatec.ExemploApis.api.v1.dto.CachorroOutputDto;
import br.com.fundatec.ExemploApis.api.v1.dto.ErroDto;
import br.com.fundatec.ExemploApis.api.v1.dto.cachorroAlterarIdadeDto;
import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.integration.ConsultarCachorroTest;
import br.com.fundatec.ExemploApis.mapper.CachorroMapper;
import br.com.fundatec.ExemploApis.service.CachorroService;

@RestController
public class CachorroController {

	private CachorroService cachorroService;
	private CachorroMapper cachorroMapper;

	public CachorroController(CachorroService cachorroService, CachorroMapper cachorroMapper) {
		this.cachorroService = cachorroService;
		this.cachorroMapper = cachorroMapper;
	}
	@GetMapping("/v1/cachorros/{id}")
	public ResponseEntity<?> ConsultarCachorro(@PathVariable Long id){		
		try {
			Cachorro cachorro = cachorroService.consultar(id);
			CachorroOutputDto cachorroOutputDto = cachorroMapper.mapearCachorroOutPutDto(cachorro);
			return ResponseEntity.ok(cachorroOutputDto);
		} catch (IllegalArgumentException e) {
			ErroDto erroDto = new ErroDto(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroDto);
		}
	}

	@GetMapping("/v1/cachorros") // anotação esta pegando
	public ResponseEntity<List<CachorroOutputDto>> getCachorros() {

		List<Cachorro> listaCachorro = cachorroService.listarTodos();
		List<CachorroOutputDto> listaCachorroDto = cachorroMapper.mapearListaCachorroOutPutDto(listaCachorro);
		return ResponseEntity.ok(listaCachorroDto);
	}

	@PostMapping("/v1/cachorros") // anotação indica que esta enviando ou incluindo
	public ResponseEntity<?> incluirCachorro(@Valid @RequestBody CachorroInputDto cachorroInputDto) {
		try {
			Cachorro cachorro = cachorroMapper.mapearCachorro(cachorroInputDto);
			cachorro = cachorroService.salvar(cachorro);
			CachorroOutputDto cachorroOutputDto = cachorroMapper.mapearCachorroOutPutDto(cachorro);
			return ResponseEntity.status(HttpStatus.CREATED).body(cachorroOutputDto);
		} catch (IllegalArgumentException e) {
			ErroDto erroDto = new ErroDto(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(erroDto);
		} catch (Exception e) {
			ErroDto erroDto = new ErroDto(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroDto);
		}
	}
	
	@PutMapping("/v1/cachorros/{id}")
	public ResponseEntity<?> alterarCachorro(@PathVariable Long id,@Valid @RequestBody CachorroInputDto cachorroInputDto) {
		try {
			Cachorro cachorro = cachorroMapper.mapearCachorro(cachorroInputDto);
			cachorro.setId(id);
			cachorro = cachorroService.salvar(cachorro);
			CachorroOutputDto cachorroOutputDto = cachorroMapper.mapearCachorroOutPutDto(cachorro);
			return ResponseEntity.ok(cachorroOutputDto);
		} catch (IllegalArgumentException e) {
			ErroDto erroDto = new ErroDto(e.getMessage());
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(erroDto);
		} catch (Exception e) {
			ErroDto erroDto = new ErroDto(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erroDto);
		}
		
	}
	
	@PatchMapping("/v1/cachorros/{id}")
	public ResponseEntity<?> alterarIdadeCachorro(@PathVariable Long id,@Valid @RequestBody cachorroAlterarIdadeDto cachorroAlterarIdadeDto){
		Cachorro cachorro = cachorroService.consultar(id);
		cachorro.setIdade(cachorroAlterarIdadeDto.getIdade());
		cachorro = cachorroService.salvar(cachorro);
		CachorroOutputDto cachorroOutputDto = cachorroMapper.mapearCachorroOutPutDto(cachorro);
		return ResponseEntity.ok(cachorroOutputDto);
	} 
	
	@DeleteMapping("/v1/cachorros/{id}")
	public ResponseEntity<?> deletarCachorro(@PathVariable Long id){
		
		try {
			cachorroService.deletar(id);
		return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			ErroDto erroDto = new ErroDto(e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroDto);
		}
	} 	

}
