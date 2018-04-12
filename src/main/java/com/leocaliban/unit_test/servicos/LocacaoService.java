package com.leocaliban.unit_test.servicos;

import static com.leocaliban.unit_test.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.leocaliban.unit_test.daos.LocacaoDAO;
import com.leocaliban.unit_test.entidades.Filme;
import com.leocaliban.unit_test.entidades.Locacao;
import com.leocaliban.unit_test.entidades.Usuario;
import com.leocaliban.unit_test.servicos.exceptions.FilmeSemEstoqueException;
import com.leocaliban.unit_test.servicos.exceptions.LocadoraException;
import com.leocaliban.unit_test.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO dao;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuário vazio.");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio.");
		}
		
		for(Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque.");
			}
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		Double valorTotal = 0d;
		
		for(int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			
			switch (i) {
				case 2: valorFilme = valorFilme * 0.75;
				break;
				
				case 3: valorFilme = valorFilme * 0.5;
				break;
				
				case 4: valorFilme = valorFilme * 0.25;
				break;
				
				case 5: valorFilme = 0.0;
				break;
			}
			valorTotal += valorFilme;
		}
		
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}
	
	//injeção de dependência do DAO
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
}