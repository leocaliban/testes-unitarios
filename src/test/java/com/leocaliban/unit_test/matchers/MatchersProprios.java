package com.leocaliban.unit_test.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}
	
	public static DataDiferencaDiasMatcher isHojeComDiferencaDias(Integer quantidadeDias) {
		return new DataDiferencaDiasMatcher(quantidadeDias);
	}
	
	public static DataDiferencaDiasMatcher isHoje() {
		return new DataDiferencaDiasMatcher(0);
	}
}
