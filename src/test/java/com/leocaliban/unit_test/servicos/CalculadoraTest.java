package com.leocaliban.unit_test.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leocaliban.unit_test.servicos.exceptions.DivisaoPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calculadora;
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {
		//cenário
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calculadora.somar(a,b);
		
		//verificação
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrarirDoisValores() {
		//cenário
		int a = 5;
		int b = 3;
		
		//ação
		int resultado = calculadora.subtrair(a,b);
		
		//verificação
		Assert.assertEquals(2, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws DivisaoPorZeroException {
		//cenário
		int a = 6;
		int b = 3;
		
		//ação
		int resultado = calculadora.dividir(a,b);
		
		//verificação
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = DivisaoPorZeroException.class)
	public void deveLancarExcecaoDivisaoPorZero() throws DivisaoPorZeroException {
		
		// cenário
		int a = 10;
		int b = 0;

		// ação
		int resultado = calculadora.dividir(a, b);

		// verificação
		Assert.assertEquals(2, resultado);

	}
}
