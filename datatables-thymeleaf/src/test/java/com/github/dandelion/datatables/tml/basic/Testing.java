package com.github.dandelion.datatables.tml.basic;

import static org.fest.assertions.Assertions.assertThat;

import com.github.dandelion.datatables.tml.ThymeleafBaseTest;

public class Testing extends ThymeleafBaseTest {

//	@Test
	public void dom(){
		executor.execute("classpath:thymeleaf/basic/dom.thtest");
		assertThat(executor.isAllOK()).isTrue();
	}
}
