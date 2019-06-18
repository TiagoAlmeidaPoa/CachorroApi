package br.com.fundatec.ExemploApis.integration;

import java.util.List;

import org.apache.http.HttpHeaders;
import org.hamcrest.Matcher;
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
import br.com.fundatec.ExemploApis.entity.Pessoa;
import br.com.fundatec.ExemploApis.entity.PorteParametro;
import br.com.fundatec.ExemploApis.repository.CachorroRepository;
import br.com.fundatec.ExemploApis.repository.PessoaRepository;
import br.com.fundatec.ExemploApis.repository.PorteParametroRepository;
import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IncluirCachorroTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired //spring boot faz instancia para nos
	private CachorroRepository cachorroRepository;
	@Autowired
	private PorteParametroRepository porteParametroRepository;
	@Autowired
	private PessoaRepository pessoaRepository;
	private Pessoa pessoa;
	
	@Before
	public void setUp() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
		cachorroRepository.deleteAll(); // aqui estou deletando todos os dados do banco
		porteParametroRepository.deleteAll();
		porteParametroRepository.save(new PorteParametro("Pequeno"));
		porteParametroRepository.save(new PorteParametro("M�dio"));
		porteParametroRepository.save(new PorteParametro("Grande"));
		pessoa = pessoaRepository.save(new Pessoa(null, "Alberto", 16));
		
	}
	
	@Test
	public void deveIncluirUmCachorro() {
		RestAssured
			.given()
			.header(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE) // aqui esta mandando
			.header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE) // aqui esta recebendo
			.body("{" + 
					"	\"nome\": \"Urso\"," + 
					"	\"raca\": \"Pastor Belga\"," + 
					"	\"porte\": \"Grande\"," + 
					"	\"idade\": 2," + 
					"   \"cpc\": \"012.345.678-90\", " +
					"	\"idPessoa\": " + pessoa.getId() +
					"}")
			.when()
			.post("/v1/cachorros")
			.then()
			.assertThat()
			.body("nome", Matchers.equalTo("Urso"))
			.body("raca", Matchers.equalTo("Pastor Belga"))
			.body("porte", Matchers.equalTo("Grande"))
			.body("idade", Matchers.equalTo(2))
			.body("id", Matchers.greaterThan(0))
			.statusCode(HttpStatus.CREATED.value());

		Cachorro cachorroIncluido = ((List<Cachorro>) cachorroRepository.findAll()).get(0);
		Assert.assertNotNull(cachorroIncluido.getPessoa());
		Assert.assertEquals("Urso", cachorroIncluido.getNome());
		Assert.assertEquals("Pastor Belga", cachorroIncluido.getRaca());
		Assert.assertEquals("Grande", cachorroIncluido.getPorte());
		Assert.assertEquals(2, cachorroIncluido.getIdade().intValue());
		Assert.assertTrue(0<cachorroIncluido.getId());
		Assert.assertTrue(cachorroRepository.count() > 0);	// verifica o banco de dados			
	}
	
	@Test
	public void deveValidarCachorroSemNome() {
		RestAssured
			.given()
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body("{" + 
					"	\"raca\": \"Pastor Belga\"," + 
					"	\"porte\": \"Grande\"," + 
					"	\"idade\": 2" +					
					"}"
					)
			.when()
			.post("/v1/cachorros")
			.then()
			.assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("errors[0].defaultMessage", Matchers.equalTo("o campo nome deve ser preenchido"));
		
		Assert.assertTrue(cachorroRepository.count() == 0);			

	}
	@Test
	public void deveValidarCpcInvalido() {
		RestAssured
			.given()
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body("{" + 
					" \"nome\": \"Urso\"," +
					"	\"raca\": \"Pastor Belga\"," + 
					"	\"porte\": \"Grande\"," + 
					"	\"idade\": 2," + 
					"   \"cpc\": \"cpc\" " +
					"}"
					)
			.when()
			.post("/v1/cachorros")
			.then()
			.assertThat()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("errors[0].defaultMessage", Matchers.equalTo("Campo cpc inv�lido"));
		
		Assert.assertTrue(cachorroRepository.count() == 0);			

	}
	
	@Test
	public void deveValidarPorteDoCachorro () {
		RestAssured
		.given()
		.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
		.body("{" + 
				" \"nome\": \"Urso\"," +
				"	\"raca\": \"Pastor Belga\"," + 
				"	\"porte\": \"grande\"," + 
				"	\"idade\": 2," + 
				"   \"cpc\": \"012.345.678-90\" " +
				"}"
				)
		.when()
		.post("/v1/cachorros")
		.then()
		.assertThat()
		.statusCode(HttpStatus.EXPECTATION_FAILED.value())
		.body("mensagem", Matchers.equalTo("porte invalido. porte deve ser Pequeno, M�dio ou Grande"));
		
		Assert.assertTrue(cachorroRepository.count() == 0);			

	}

}
