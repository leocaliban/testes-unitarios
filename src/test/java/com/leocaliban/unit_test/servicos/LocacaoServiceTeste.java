package com.leocaliban.unit_test.servicos;

import static com.leocaliban.unit_test.builders.FilmeBuilder.umFilme;
import static com.leocaliban.unit_test.builders.LocacaoBuilder.umLocacao;
import static com.leocaliban.unit_test.builders.UsuarioBuilder.umUsuario;
import static com.leocaliban.unit_test.matchers.MatchersProprios.caiNumaSegunda;
import static com.leocaliban.unit_test.matchers.MatchersProprios.isHoje;
import static com.leocaliban.unit_test.matchers.MatchersProprios.isHojeComDiferencaDias;
import static com.leocaliban.unit_test.utils.DataUtils.isMesmaData;
import static com.leocaliban.unit_test.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.leocaliban.unit_test.builders.FilmeBuilder;
import com.leocaliban.unit_test.daos.LocacaoDAO;
import com.leocaliban.unit_test.entidades.Filme;
import com.leocaliban.unit_test.entidades.Locacao;
import com.leocaliban.unit_test.entidades.Usuario;
import com.leocaliban.unit_test.servicos.exceptions.FilmeSemEstoqueException;
import com.leocaliban.unit_test.servicos.exceptions.LocadoraException;
import com.leocaliban.unit_test.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocacaoService.class })
public class LocacaoServiceTeste {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule //referente ao FilmeSemEstoque3, forma nova
	public ExpectedException exceptedException = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);	
		service = PowerMockito.spy(service);
	}
	
	@Test
	public void teste() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
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
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
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
	public void deveAlugarFilme() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(20, 4, 2018));
		/**
		 * mock de métodos estáticos
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 20);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2018);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		//acao
		Locacao locacao;
		try {//quando o teste não precisar lançar exceção, não precisa usar try
			locacao = service.alugarFilme(usuario, filmes);
			
			//verificacao
			error.checkThat(locacao.getValor(), is(equalTo(5.0)));
			
			//error.checkThat(locacao.getDataLocacao(), isHoje());
			//error.checkThat(locacao.getDataRetorno(), isHojeComDiferencaDias(1));
			
			error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(20, 4, 2018)), is(true));
			error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(21, 4, 2018)), is(true));
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
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilmeSemEstoque().agora()); //usando o builder específico
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	//teste robusto, onde se tem o total controle sobre o que se espera, no caso das exceções.
	@Test
	public void testeLocacao_FilmeSemEstoque2() throws LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
		
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
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
		
		exceptedException.expect(FilmeSemEstoqueException.class);
		exceptedException.expectMessage("Filme sem estoque.");
		
		//acao
		service.alugarFilme(usuario, filmes);
		
	}
	
	
	//robusta
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		List<Filme> filmes = Arrays.asList(umFilme().agora());

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
		Usuario usuario = umUsuario().agora();
		
		exceptedException.expect(LocadoraException.class);
		exceptedException.expectMessage("Filme vazio.");
		// acao
		service.alugarFilme(usuario, null);
		
	}


	@Test
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws Exception{
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(21, 4, 2018));
		
		/**
		 * mock de métodos estáticos
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 21);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2018);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		//assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY)); (OUTRA FORMA)
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
//		PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();
		
		/**
		 * verificação de mock de métodos estáticos
		 */
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
	}
	
	@Test
	public void naoDeveAlugarFilmeParaUsuarioComSaldoNegativo() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiSaldoNegativo(usuario)).thenReturn(true);
				
		//acao
		try {
			service.alugarFilme(usuario, filmes);
		//verificacao
			Assert.fail();
		}
		catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário está com saldo negativo."));
		}
		
		Mockito.verify(spc).possuiSaldoNegativo(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Locacao> locacoes = Arrays.asList(umLocacao()
				.comUsuario(usuario)
				.atrasada().agora());
		
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		Mockito.verify(emailService).notificarAtraso(usuario);
	}
	
	
	@Test
	public void deveTratarErroNoSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spc.possuiSaldoNegativo(usuario)).thenThrow(new Exception("FALHOU"));
		
		exceptedException.expect(LocadoraException.class);
		exceptedException.expectMessage("Problema com SPC.");
		
		//acao
		service.alugarFilme(usuario, filmes);
		
		//verificacao
	}
	
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = umLocacao().agora();

		// acao
		service.prorrogarLocacao(locacao, 3);

		// verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), isHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), isHojeComDiferencaDias(3));
	}
	
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}
		
}
