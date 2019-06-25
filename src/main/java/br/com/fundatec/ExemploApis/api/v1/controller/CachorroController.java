package br.com.fundatec.ExemploApis.api.v1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fundatec.ExemploApis.api.v1.dto.CachorroInputDto;
import br.com.fundatec.ExemploApis.api.v1.dto.CachorroOutputDto;
import br.com.fundatec.ExemploApis.api.v1.dto.ErroDto;
import br.com.fundatec.ExemploApis.api.v1.dto.cachorroAlterarIdadeDto;
import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.entity.Pessoa;
import br.com.fundatec.ExemploApis.mapper.CachorroMapper;
import br.com.fundatec.ExemploApis.service.CachorroService;
import br.com.fundatec.ExemploApis.service.PessoaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CachorroController {

	private CachorroService cachorroService;
	private CachorroMapper cachorroMapper;
	private PessoaService pessoaService;

	
	public CachorroController(CachorroService cachorroService, CachorroMapper cachorroMapper,
			PessoaService pessoaService) {
		this.cachorroService = cachorroService;
		this.cachorroMapper = cachorroMapper;
		this.pessoaService = pessoaService;
	}

	@GetMapping("/v1/cachorros/{id}")
	@ApiOperation(value = "Consulta um cachorro",
    notes = "Consulta um cachorro pelo o id passado como parâmetro, caso não encontre retorna um codigo de erro")
	@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Cachorro retornado com sucesso", response = CachorroOutputDto.class),
	})
	
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
	
	@GetMapping("/v1/cachorros/exercicio")
	public ResponseEntity<List<CachorroOutputDto>> getCachorrosNomeIdade(@RequestParam String nome, 
			@RequestParam Integer idadeMinima, 
			@RequestParam Integer idadeMaxima){
		List<Cachorro> listaCachorroNomeIdade = cachorroService.listar(nome,idadeMinima,idadeMaxima);
		List<CachorroOutputDto> listaCachorroNomeIdadeDto = cachorroMapper.mapearListaCachorroOutPutDto(listaCachorroNomeIdade);
		return ResponseEntity.ok(listaCachorroNomeIdadeDto);
	}

	@GetMapping("/v1/cachorros") // anotação esta pegando
	public ResponseEntity<List<CachorroOutputDto>> getCachorros(@RequestParam(defaultValue ="") String nome) {

		List<Cachorro> listaCachorro = cachorroService.listar(nome);
		List<CachorroOutputDto> listaCachorroDto = cachorroMapper.mapearListaCachorroOutPutDto(listaCachorro);
		return ResponseEntity.ok(listaCachorroDto);
	}

	@PostMapping("/v1/cachorros") // anotação indica que esta enviando ou incluindo
	
	@GetMapping("/v1/cachorros/{id}")
	@ApiOperation(value = "Inclui um cachorro",
    notes = "Inclui um cachorro no banco")
	@ApiResponses(value = {
    @ApiResponse(code = 201, message = "Cachorro incluido com sucesso", response = CachorroOutputDto.class),
	})
	public ResponseEntity<?> incluirCachorro(@Valid @RequestBody CachorroInputDto cachorroInputDto) {
		try {
			Cachorro cachorro = cachorroMapper.mapearCachorro(cachorroInputDto);
			if(cachorroInputDto.getIdPessoa() !=null) {				
				Pessoa pessoa = pessoaService.consultar(cachorroInputDto.getIdPessoa());
				cachorro.setPessoa(pessoa);
			}
			
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
	
	@GetMapping("/v1/cachorros/{id}")
	@ApiOperation(value = "Alterar Cachorro",
    notes = "Alterar um cachorro pelo o id passado como parâmetro, caso não encontre retorna um codigo de erro")
	@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Cachorro Alterado com sucesso", response = CachorroOutputDto.class),
	})
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
	
	@GetMapping("/v1/cachorros/{id}")
	@ApiOperation(value = "Alterar a idade de um Cachorro",
    notes = "Alterar a idade de um cachorro pelo id passado como parâmetro")
	@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Idade do Cachorro Alterada com sucesso", response = CachorroOutputDto.class),
	})
	public ResponseEntity<?> alterarIdadeCachorro(@PathVariable Long id,@Valid @RequestBody cachorroAlterarIdadeDto cachorroAlterarIdadeDto){
		Cachorro cachorro = cachorroService.consultar(id);
		cachorro.setIdade(cachorroAlterarIdadeDto.getIdade());
		cachorro = cachorroService.salvar(cachorro);
		CachorroOutputDto cachorroOutputDto = cachorroMapper.mapearCachorroOutPutDto(cachorro);
		return ResponseEntity.ok(cachorroOutputDto);
	} 
	
	@GetMapping("/v1/cachorros/lista-todos")
	public ResponseEntity<?> listarTodos(@RequestParam(required=false) Long id, 
										@RequestParam(required=false) String nome, 
										@RequestParam(required=false) String raca, 
										@RequestParam(required=false) String porte, 
										@RequestParam(required=false) Integer idade ){
		List<Cachorro> listaCachorros = cachorroService.listar(id, nome, raca, porte, idade);
		List<CachorroOutputDto> listaCachorroOutputDto = cachorroMapper.mapearListaCachorroOutPutDto(listaCachorros);
		return ResponseEntity.ok(listaCachorroOutputDto);
	}
	
	
	@DeleteMapping("/v1/cachorros/{id}")
	
	@GetMapping("/v1/cachorros/{id}")
	@ApiOperation(value = "Deleta um Cachorro",
    notes = "Deleta um cachorro pelo id passado como parâmetro, caso não encontre retorna um codigo de erro")
	@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Cachorro Deletado com sucesso", response = CachorroOutputDto.class),
	})
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
