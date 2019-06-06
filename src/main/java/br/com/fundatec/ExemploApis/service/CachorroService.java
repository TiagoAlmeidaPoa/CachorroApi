package br.com.fundatec.ExemploApis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.repository.CachorroRepository;

@Service
public class CachorroService {
	
	private CachorroRepository cachorroRepository;
	private PorteParametroService porteParametroService;
	
	

	public CachorroService(CachorroRepository cachorroRepository, PorteParametroService porteParametroService) {
		this.cachorroRepository = cachorroRepository;
		this.porteParametroService = porteParametroService;
	}

	public List<Cachorro> listarTodos(){
		return (List<Cachorro>) cachorroRepository.findAll();
	}
	
	public Cachorro salvar(Cachorro cachorro) { // aqui estou incluindo no banco
		validarSalvarCachorro(cachorro);
		return cachorroRepository.save(cachorro);
	}
	
	private void validarSalvarCachorro(Cachorro cachorro) {
		validarPorte(cachorro);
	}

	private void validarPorte(Cachorro cachorro) {
		if(!porteParametroService.porteValido(cachorro.getPorte())) {
			throw new IllegalArgumentException("porte invalido. porte deve ser Pequeno, Médio ou Grande");
		}		
	}

}
