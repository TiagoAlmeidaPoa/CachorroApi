package br.com.fundatec.ExemploApis.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.repository.CachorroRepository;

@Service
public class CachorroService {
	
	private CachorroRepository cachorroRepository;
	
	public CachorroService(CachorroRepository cachorroRepository) {
		this.cachorroRepository = cachorroRepository;
	}

	public List<Cachorro> listarTodos(){
		return (List<Cachorro>) cachorroRepository.findAll();
	}
	
	public Cachorro incluir(Cachorro cachorro) { // aqui estou incluindo no banco
		return cachorroRepository.save(cachorro);
	}

}
