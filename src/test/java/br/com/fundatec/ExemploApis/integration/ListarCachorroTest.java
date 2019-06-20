package br.com.fundatec.ExemploApis.integration;


import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.fundatec.ExemploApis.api.v1.dto.CachorroOutputDto;
import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.repository.CachorroRepository;
import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
public class ListarCachorroTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private CachorroRepository cachorroRepository;
	
	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "Http://localhost";
		cachorroRepository.deleteAll();
	}
	
	@Test
	public void deveBuscarUmaListaDeCachorros() {
		
		cachorroRepository.save(new Cachorro(null,"Bob","Poodle","Medio",15));
		cachorroRepository.save(new Cachorro(null,"Rex","Pitbull","Grande",4));

		
		
		
		RestAssured
				.get("/v1/cachorros")
				.then()
				.body("nome", Matchers.hasItems("Bob", "Rex"))
				.body("raca", Matchers.hasItems("Poodle", "Pitbull"))
				.body("porte", Matchers.hasItems("Medio", "Grande"))
				.body("idade", Matchers.hasItems(15, 4))
				.statusCode(HttpStatus.OK.value()); // status 200
	}
	
	@Test
	public void deveListarCachorroFiltrandoPorNome() {
		
		cachorroRepository.save(new Cachorro(null,"Bob","Poodle","Médio",15));
		cachorroRepository.save(new Cachorro(null,"Rex","Pitbull","Grande",4));
		cachorroRepository.save(new Cachorro(null,"Roberto","Chihuahua","Pequeno",10));		
		
		CachorroOutputDto[] resultado = RestAssured
			.given()
			.when()
			.get("/v1/cachorros?nome=ob")
			.then()
			.assertThat()			
			.statusCode(HttpStatus.OK.value())
			.extract()
			.as(CachorroOutputDto[].class);

		List<String> nomesEsperados = Arrays.asList("Bob", "Roberto");
		List<String> racasEsperadas = Arrays.asList("Poodle", "Chihuahua");
		List<String> portesEsperados = Arrays.asList("Médio", "Pequeno");
		List<Integer> idadesEsperadas = Arrays.asList(15, 10);
		
		Assert.assertEquals(2, resultado.length);
		
		for (CachorroOutputDto cachorroOutputDto : resultado) {
			Assert.assertTrue("Não encontrou o nome " + cachorroOutputDto.getNome() + "na lista de esperados", nomesEsperados.contains(cachorroOutputDto.getNome()));
			Assert.assertTrue(racasEsperadas.contains(cachorroOutputDto.getRaca()));
			Assert.assertTrue(portesEsperados.contains(cachorroOutputDto.getPorte()));
			Assert.assertTrue(idadesEsperadas.contains(cachorroOutputDto.getIdade()));
		}
	}
	
	@Test
	public void deveListarNomeIdadeMinimaEIdadeMaxima() {
		
		cachorroRepository.save(new Cachorro(null,"Bob","Poodle","Médio",15));
		cachorroRepository.save(new Cachorro(null,"Rex","Pitbull","Grande",4));
		cachorroRepository.save(new Cachorro(null,"Roberto","Chihuahua","Pequeno",10));	
		
		CachorroOutputDto[] resultado = RestAssured
				.given()
				.when()
				.get("/v1/cachorros?nome=Rex&idadeMinima=1&idadeMaxima=15")
				.then()
				.assertThat()			
				.statusCode(HttpStatus.OK.value())
				.extract()
				.as(CachorroOutputDto[].class);
		
		List<String> nomesEsperados = Arrays.asList("Rex");
		List<String> racasEsperadas = Arrays.asList("Pitbull");
		List<String> portesEsperados = Arrays.asList("Grande");
		List<Integer> idadesEsperadas = Arrays.asList(4);
		
		Assert.assertEquals(1 , resultado.length);
		
		for (CachorroOutputDto cachorroOutputDto : resultado) {
			
			Assert.assertTrue("Não encontrou o nome " + cachorroOutputDto.getNome() + " na lista de esperados", nomesEsperados.contains(cachorroOutputDto.getNome()));
			Assert.assertTrue(racasEsperadas.contains(cachorroOutputDto.getRaca()));
			Assert.assertTrue(portesEsperados.contains(cachorroOutputDto.getPorte()));
			Assert.assertTrue(idadesEsperadas.contains(cachorroOutputDto.getIdade()));
			
		}

		
	}
	
}
