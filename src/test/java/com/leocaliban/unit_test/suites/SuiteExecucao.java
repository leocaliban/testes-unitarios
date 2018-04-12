package com.leocaliban.unit_test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.leocaliban.unit_test.servicos.CalculadoraTest;
import com.leocaliban.unit_test.servicos.CalculoValorLocacaoTest;
import com.leocaliban.unit_test.servicos.LocacaoServiceTeste;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTeste.class
})
public class SuiteExecucao {
	//SUITE.
}
