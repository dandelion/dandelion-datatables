package com.github.dandelion.datatables.extras.spring3.ajax;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.constants.DTConstants;

public class DatatablesCriteriasResolverTest {

	private MockHttpServletRequest request;
	private MockMvc mockMvc;

	@Before
	public void setup(){
		this.mockMvc = standaloneSetup(new MockController())
				.setCustomArgumentResolvers(new DatatablesCriteriasMethodArgumentResolver())
				.build();
	}
	
	@Test
	public void should_return_criteria() throws Exception {
		request = new MockHttpServletRequest();
		String dc = DatatablesCriterias.getFromRequest(request).toString(); 
		
		this.mockMvc
			.perform(get("/param"))
			.andExpect(content().string(dc.toString()));
	}
	
	@Test
	public void should_return_criteria_with_search() throws Exception {
		request = new MockHttpServletRequest();
		request.setParameter(DTConstants.DT_S_SEARCH, "search");
		String dc = DatatablesCriterias.getFromRequest(request).toString(); 
		
		this.mockMvc
			.perform(get("/param")
				.param(DTConstants.DT_S_SEARCH, "search"))
			.andExpect(content().string(dc.toString()));
	}
}
