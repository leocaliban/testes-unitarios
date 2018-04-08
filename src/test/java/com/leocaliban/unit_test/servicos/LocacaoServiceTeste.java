package com.leocaliban.unit_test.servicos;

import static com.leocaliban.unit_test.utils.DataUtils.isMesmaData;
import static com.leocaliban.unit_test.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import com.leocaliban.unit_test.entidades.Filme;
import com.leocaliban.unit_test.entidades.Locacao;
import com.leocaliban.unit_test.entidades.Usuario;
import com.leocaliban.unit_test.servicos.exceptions.FilmeSemEstoqueException;
import com.leocaliban.unit_test.utils.DataUtils;

public class LocacaoServiceTeste {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule //referente ao FilmeSemEstoque3, forma nova
	public ExpectedException exceptedException = ExpectedException.none();
	
	@Test
	public void teste() throws FilmeSemEstoqueException {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Rafael");
		Filme filme = new Filme("Anabelle", 2, 5.0);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		// verificacao
		Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));

	}
	
	@Test
	public void testeAssertThat() throws FilmeSemEstoqueException {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Rafael");
		Filme filme = new Filme("Anabelle", 2, 5.0);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filme);

		// verificacao
		assertThat(locacao.getValor(), is(equalTo(5.0)));
		assertThat(locacao.getValor(), is(not(6.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

	}
	
	//Teste com Rule ErrorCollector
	@Test
	public void testeLocacao() {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Rafael");
		Filme filme = new Filme("Anabelle", 2, 5.0);
		
		//acao
		Locacao locacao;
		try {//quando o teste não precisar lançar exceção, não precisa usar try
			locacao = service.alugarFilme(usuario, filme);
			
			//verificacao
			error.checkThat(locacao.getValor(), is(equalTo(5.0)));
			error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
			error.checkThat(isMesmaData(locacao.getDataRetorno(),obterDataComDiferencaDias(1)), is(true));
		} catch (FilmeSemEstoqueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	//teste simplificado, elegante, onde o JUnit trata o que se espera.
	@Test(expected=FilmeSemEstoqueException.class)
	public void testeLocacao_FilmeSemEstoque() throws FilmeSemEstoqueException {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Rafael");
		Filme filme = new Filme("Anabelle", 0, 5.0);
		
		//acao
		service.alugarFilme(usuario, filme);
	}
	
	//teste robusto, onde se tem o total controle sobre o que se espera, no caso das exceções.
	@Test
	public void testeLocacao_FilmeSemEstoque2() {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Rafael");
		Filme filme = new Filme("Anabelle", 0, 5.0);
		
		//acao
		try {
			service.alugarFilme(usuario, filme);
			fail("Deve ser lançada uma Exceção.");
		} 
		catch (FilmeSemEstoqueException e) {
			assertThat(e.getMessage(), is("Filme sem estoque."));
		}
	}
	
	//forma nova usando rules
	@Test
	public void testeLocacao_FilmeSemEstoque3() throws FilmeSemEstoqueException {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Rafael");
		Filme filme = new Filme("Anabelle", 0, 5.0);
		
		exceptedException.expect(FilmeSemEstoqueException.class);
		exceptedException.expectMessage("Filme sem estoque.");
		
		//acao
		service.alugarFilme(usuario, filme);
		
	}
	
}
