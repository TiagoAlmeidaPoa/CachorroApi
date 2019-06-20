package br.com.fundatec.ExemploApis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.repository.CachorroRepository;
import javassist.NotFoundException;

@Service
public class CachorroService {
	
	private CachorroRepository cachorroRepository;
	private PorteParametroService porteParametroService;
	
	

	public CachorroService(CachorroRepository cachorroRepository, PorteParametroService porteParametroService) {
		this.cachorroRepository = cachorroRepository;
		this.porteParametroService = porteParametroService;
	}

	public List<Cachorro> listar(String nome){
		return cachorroRepository.findByNomeContainingIgnoringCase(nome);
	}
	
	public Cachorro salvar(Cachorro cachorro) { // aqui estou incluindo no banco
		validarSalvarCachorro(cachorro);
		return cachorroRepository.save(cachorro);
	}
	public void deletar(Long id) { // aqui estou deletando do banco
		
		if(cachorroRepository.existsById(id)) {
			cachorroRepository.deleteById(id);
		}else {
			throw new IllegalArgumentException("Cachorrro não existe para este ID");
		}
		 
	}
	
	private void validarSalvarCachorro(Cachorro cachorro) {
		validarPorte(cachorro);
	}

	private void validarPorte(Cachorro cachorro) {
		if(!porteParametroService.porteValido(cachorro.getPorte())) {
			throw new IllegalArgumentException("porte invalido. porte deve ser Pequeno, Médio ou Grande");
		}		
	}

	public Cachorro consultar(Long id) {
		
		return cachorroRepository.findById(id)
				.orElseThrow(()-> new IllegalArgumentException("Não encontrou cachorro para o id: "+id));
	}

	public List<Cachorro> listar(String nome, Integer idadeMinima, Integer idadeMaxima) {
		
		return cachorroRepository.findByNomeContainingIgnoringCaseAndIdadeBetween(nome,idadeMinima,idadeMaxima);
		
	}

}
