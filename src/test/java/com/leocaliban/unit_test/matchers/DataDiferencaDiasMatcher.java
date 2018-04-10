package com.leocaliban.unit_test.matchers;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.leocaliban.unit_test.utils.DataUtils;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer quantidadeDias;
	
	public DataDiferencaDiasMatcher(Integer quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	public void describeTo(Description description) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean matchesSafely(Date item) {
		return DataUtils.isMesmaData(item, DataUtils.obterDataComDiferencaDias(quantidadeDias));
	}

}
