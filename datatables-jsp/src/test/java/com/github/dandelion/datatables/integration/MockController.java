package com.github.dandelion.datatables.integration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.utils.Mock;
import com.github.dandelion.datatables.utils.Person;

/**
 * 
 * 
 * @author Thibault Duchateau
 */
@Controller
@RequestMapping(method = RequestMethod.GET)
public class MockController {

	@Autowired
	private MockService mockService;
	
	@RequestMapping(value = "/basic/{page}")
	public String goToBasicExample(@PathVariable String page) {
		return "basic/" + page;
	}

	@RequestMapping(value = "/advanced/{page}")
	public String goToAdvancedExample(@PathVariable String page) {
		return "advanced/" + page;
	}

	@RequestMapping(value = "/ajax/{page}")
	public String goToAjaxExample(@PathVariable String page) {
		return "ajax/" + page;
	}

	@RequestMapping(value = "/features/{page}")
	public String goToFeatureExample(@PathVariable String page) {
		return "features/" + page;
	}

	@RequestMapping(value = "/themes/{page}")
	public String goToThemeExample(@PathVariable String page) {
		return "themes/" + page;
	}

	@RequestMapping(value = "/persons", produces = "application/json")
	public @ResponseBody
	List<Person> findAll(HttpServletRequest request) {
		return Mock.persons;
	}
	
	@RequestMapping(value = "/persons1", method = RequestMethod.GET)
	public @ResponseBody
	DatatablesResponse<Person> findAllForDataTables(HttpServletRequest request) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<Person> persons = mockService.getData(criterias);
		return DatatablesResponse.build(persons, criterias);
	}
}
