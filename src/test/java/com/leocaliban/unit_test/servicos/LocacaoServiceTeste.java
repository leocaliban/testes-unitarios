package com.leocaliban.unit_test.servicos;

import static com.leocaliban.unit_test.utils.DataUtils.isMesmaData;
import static com.leocaliban.unit_test.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import com.leocaliban.unit_test.entidades.Filme;
import com.leocaliban.unit_test.entidades.Locacao;
import com.leocaliban.unit_test.entidades.Usuario;
import com.leocaliban.unit_test.servicos.exceptions.FilmeSemEstoqueException;
import com.leocaliban.unit_test.servicos.exceptions.LocadoraException;
import com.leocaliban.unit_test.utils.DataUtils;

public class LocacaoServiceTeste {
	
	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule //referente ao FilmeSemEstoque3, forma nova
	public ExpectedException exceptedException = ExpectedException.none();
	
	@Before
	public void criarCenario() {
		service = new LocacaoService();
	}
	
	@Test
	public void teste() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 2, 5.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

	}
	
	@Test
	public void testeAssertThat() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 2, 5.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), is(not(6.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}
	
	//Teste com Rule ErrorCollector
	@Test
	public void deveAlugarFilme() {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 2, 5.0));
		
		//acao
		Locacao locacao;
		try {//quando o teste não precisar lançar exceção, não precisa usar try
			locacao = service.alugarFilme(usuario, filmes);
			
			//verificacao
			error.checkThat(locacao.getValor(), is(equalTo(5.0)));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(isMesmaData(locacao.getDataRetorno(),obterDataComDiferencaDias(1)), is(true));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	//teste simplificado, elegante, onde o JUnit trata o que se espera.
	//Indicado usar quando o projeto possui exceções personalizadas, que serão direcionadas ao teste em específico.
	@Test(expected=FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws FilmeSemEstoqueException, LocadoraException  {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 0, 5.0));
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	//teste robusto, onde se tem o total controle sobre o que se espera, no caso das exceções.
	@Test
	public void testeLocacao_FilmeSemEstoque2() throws LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 0, 5.0));
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
			fail("Deve ser lançada uma Exceção.");
		} 
		catch (FilmeSemEstoqueException e) {
			assertThat(e.getMessage(), is("Filme sem estoque."));
		}
	}
	
	//forma nova usando rules
	@Test
	public void testeLocacao_FilmeSemEstoque3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 0, 5.0));
		
		exceptedException.expect(FilmeSemEstoqueException.class);
		exceptedException.expectMessage("Filme sem estoque.");
		
		//acao
		service.alugarFilme(usuario, filmes);
		
	}
	
	
	//robusta
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 2, 5.0));

		// acao
		try {
			service.alugarFilme(null, filmes);
			fail();
		}
		catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário vazio."));
		}
	
	}
	
	
	//nova
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Rafael");
		
		exceptedException.expect(LocadoraException.class);
		exceptedException.expectMessage("Filme vazio.");
		// acao
		service.alugarFilme(usuario, null);
		
	}
	
	
	
	@Test
	public void devePagar75pctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Rafael");
		List<Filme> filmes = Arrays.asList(new Filme("Anabelle", 2, 4.0), 
											new Filme("Casa de Cera", 2, 4.0),
											new Filme("Rambo", 1, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(resultado.getValor(), is(11d));
	}
		
}
