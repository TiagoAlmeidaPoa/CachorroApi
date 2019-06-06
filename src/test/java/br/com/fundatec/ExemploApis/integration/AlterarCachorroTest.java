package br.com.fundatec.ExemploApis.integration;

import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.fundatec.ExemploApis.entity.Cachorro;
import br.com.fundatec.ExemploApis.entity.PorteParametro;
import br.com.fundatec.ExemploApis.repository.CachorroRepository;
import br.com.fundatec.ExemploApis.repository.PorteParametroRepository;
import io.restassured.RestAssured;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class AlterarCachorroTest {

	@LocalServerPort
	private int port;

	@Autowired // spring boot faz instancia para nos
	private CachorroRepository cachorroRepository;
	@Autowired
	private PorteParametroRepository porteParametroRepository;

	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
		cachorroRepository.deleteAll(); // aqui estou deletando todos os dados do banco
		porteParametroRepository.deleteAll();
		porteParametroRepository.save(new PorteParametro("Pequeno"));
		porteParametroRepository.save(new PorteParametro("M�dio"));
		porteParametroRepository.save(new PorteParametro("Grande"));

	}

	@Test
	public void deveAlterarCachorro() {
		Cachorro cachorro = new Cachorro(null, "Tobias", "Beagle", "Pequeno", 2);
		 		cachorro = cachorroRepository.save(cachorro);
		 		
		 		RestAssured
		 		.given()
		 		.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
		 		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		 		.body("{" + 
		 				"	\"nome\": \"Tobias Ferreira\"," + 
		 				"	\"raca\": \"Chow chow\"," + 
		 				"	\"porte\": \"Grande\"," + 
		 				"	\"idade\": 6" + 
		 				"}")
		 		.when()
		 		.put("/v1/cachorros/{id}",cachorro.getId())
		 		.then()
		 		.assertThat()
		 		.statusCode(HttpStatus.OK.value())
		 		.body("id", Matchers.equalTo(cachorro.getId().intValue()))
		 		.body("nome", Matchers.equalTo("Tobias Ferreira"))
		 		.body("raca", Matchers.equalTo("Chow chow"))
		 		.body("porte", Matchers.equalTo("Grande"))
		 		.body("idade", Matchers.equalTo(6));
		 
		Cachorro cachorroAlterado = cachorroRepository.findById(cachorro.getId()).orElse(null);
		Assert.assertEquals("Tobias Ferreira", cachorroAlterado.getNome());
		Assert.assertEquals("Chow chow", cachorroAlterado.getRaca());
		Assert.assertEquals("Grande", cachorroAlterado.getPorte());
		Assert.assertEquals(6, cachorroAlterado.getIdade().intValue());
		



		
	}

}
