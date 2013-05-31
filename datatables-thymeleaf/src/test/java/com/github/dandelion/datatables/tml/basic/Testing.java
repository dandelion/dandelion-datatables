package com.github.dandelion.datatables.tml.basic;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.github.dandelion.datatables.tml.ThymeleafBaseTest;

public class Testing extends ThymeleafBaseTest {

//	@Test
	public void test(){
		executor.execute("classpath:thymeleaf/basic/test01.thtest");
		assertThat(executor.isAllOK()).isTrue();
	}
	
//	@Test
	public void dom(){
		executor.execute("classpath:thymeleaf/basic/dom.thtest");
		assertThat(executor.isAllOK()).isTrue();
	}
}
