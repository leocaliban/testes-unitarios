package com.leocaliban.unit_test.servicos;

import static com.leocaliban.unit_test.utils.DataUtils.adicionarDias;

import java.util.Date;

import com.leocaliban.unit_test.entidades.Filme;
import com.leocaliban.unit_test.entidades.Locacao;
import com.leocaliban.unit_test.entidades.Usuario;
import com.leocaliban.unit_test.servicos.exceptions.FilmeSemEstoqueException;
import com.leocaliban.unit_test.servicos.exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuário vazio.");
		}
		
		if(filme == null) {
			throw new LocadoraException("Filme vazio.");
		}
		
		if(filme.getEstoque() == 0) {
			throw new FilmeSemEstoqueException("Filme sem estoque.");
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}