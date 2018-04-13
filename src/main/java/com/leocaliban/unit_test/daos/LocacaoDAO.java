package com.leocaliban.unit_test.daos;

import java.util.List;

import com.leocaliban.unit_test.entidades.Locacao;

public interface LocacaoDAO {
	
	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();
}
