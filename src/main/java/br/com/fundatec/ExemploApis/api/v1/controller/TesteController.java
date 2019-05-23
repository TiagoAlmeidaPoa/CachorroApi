package br.com.fundatec.ExemploApis.api.v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {
	
	@GetMapping("/hellow-world")
	public ResponseEntity<String> meuPrimeiroTest(){
		return ResponseEntity.ok("hellow world");
	}

}
