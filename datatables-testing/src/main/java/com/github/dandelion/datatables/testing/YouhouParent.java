package com.github.dandelion.datatables.testing;

import org.fluentlenium.core.FluentAdapter;
import org.junit.Test;

public class YouhouParent extends FluentAdapter {

	@Test
	public void testParent1(){
		System.out.println("=> contenu de testParent1");
	}
	
	@Test
	public void testParent2(){
		System.out.println("=> contenu de testParent2");
	}
}
