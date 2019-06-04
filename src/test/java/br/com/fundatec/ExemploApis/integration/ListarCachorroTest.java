package br.com.fundatec.ExemploApis.integration;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;import org.springframework.web.client.HttpStatusCodeException;

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

}
