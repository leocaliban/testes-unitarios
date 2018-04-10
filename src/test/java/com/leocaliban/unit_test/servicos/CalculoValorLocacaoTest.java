package com.leocaliban.unit_test.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.leocaliban.unit_test.entidades.Filme;
import com.leocaliban.unit_test.entidades.Locacao;
import com.leocaliban.unit_test.entidades.Usuario;
import com.leocaliban.unit_test.servicos.exceptions.FilmeSemEstoqueException;
import com.leocaliban.unit_test.servicos.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	private LocacaoService service;
	
	@Parameter
	public List<Filme>filmes;
	
	@Parameter(value = 1)
	public Double valorLocacao;
	
	@Parameter(value = 2)
	public String cenario;
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	private static Filme filme1 = new Filme("Anabelle",2,4.0);
	private static Filme filme2 = new Filme("Casa de Cera",2,4.0);
	private static Filme filme3 = new Filme("Rambo",1,4.0);
	private static Filme filme4 = new Filme("Pearl Harbor", 2, 4.0);
	private static Filme filme5 = new Filme("The Mummy", 2, 4.0);
	private static Filme filme6 = new Filme("Fury", 5, 4.0);
	private static Filme filme7 = new Filme("Baby Driver", 5, 6.0);
	
	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 8d, "2 Filmes: Sem Desconto"},
			{Arrays.asList(filme1, filme2, filme3), 11d, "3 Filmes: 25%"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 13d, "4 Filmes: 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14d, "5 Filmes: 75%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14d, "6 Filmes: 100%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 20d, "7 Filmes: Sem Desconto"}
		});
	}

	@Test
	public void deveCalcularValorLocacaoComDescontos() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Rafael");	
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(resultado.getValor(), is(valorLocacao));
	}
}
