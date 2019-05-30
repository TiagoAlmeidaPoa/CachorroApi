package br.com.fundatec.ExemploApis.api.v1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fundatec.ExemploApis.api.v1.dto.CachorroInputDto;
import br.com.fundatec.ExemploApis.api.v1.dto.CachorroOutputDto;
import br.com.fundatec.ExemploApis.entity.Cachorro;
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

	@GetMapping("/v1/cachorros") // anotação esta pegando
	public ResponseEntity<List<CachorroOutputDto>> getCachorros() {
//		List<CachorroDto> listaCachorro = new ArrayList<CachorroDto>();
//		listaCachorro.add(new CachorroDto("Bob", "poodle", "medio", 15));
//		listaCachorro.add(new CachorroDto("Goku", "vira-lata", "grande", 3));
//		listaCachorro.add(new CachorroDto("Rex", "pitbull", "grande", 4));
//		listaCachorro.add(new CachorroDto("Bilu", "slsichinha", "pequeno", 2));
//		listaCachorro.add(new CachorroDto("Amarelo", "Golden Retriever", "Grande", 1));

		List<Cachorro> listaCachorro = cachorroService.listarTodos();
		List<CachorroOutputDto> listaCachorroDto = cachorroMapper.mapearListaCachorroOutPutDto(listaCachorro);
		return ResponseEntity.status(HttpStatus.OK).body(listaCachorroDto);
	}

	@PostMapping("/v1/cachorros") // anotação indica que esta enviando ou incluindo
	public ResponseEntity<CachorroOutputDto> incluirCachorro(@RequestBody CachorroInputDto cachorroInputDto) {
		Cachorro cachorro = cachorroMapper.mapearCachorro(cachorroInputDto);
		cachorro = cachorroService.incluir(cachorro);
		CachorroOutputDto cachorroOutputDto = cachorroMapper.mapearCachorroOutPutDto(cachorro);
		return ResponseEntity.status(HttpStatus.CREATED).body(cachorroOutputDto);
	}

}
