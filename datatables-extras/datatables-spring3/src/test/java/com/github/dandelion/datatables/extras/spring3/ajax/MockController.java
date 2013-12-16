package com.github.dandelion.datatables.extras.spring3.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.mock.Person;

/**
 * Controller used in the Spring web application.
 * 
 * @author Thibault Duchateau
 */
@Controller
@RequestMapping(method = RequestMethod.GET)
public class MockController {

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
	
	@RequestMapping(value = "/param")
	public @ResponseBody String findWithParam(@DatatablesParams DatatablesCriterias criterias){
		return criterias.toString();
	}
}