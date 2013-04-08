/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.dandelion.datatables.integration;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.github.dandelion.datatables.mock.Mock;
import com.github.dandelion.datatables.mock.Person;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;

public class ThymeleafServlet extends HttpServlet {

	private static final long serialVersionUID = 5556618877116829753L;
	private static TemplateEngine templateEngine;
	
	static {
		initializeTemplateEngine();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// Get the template's name to process
		String templateName = request.getRequestURL().substring(request.getRequestURL().lastIndexOf("-") + 1, request.getRequestURL().length());
		
		// Adapt the response headers
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		// Fill the request with some lists
		request.setAttribute("persons", Mock.persons);
		request.setAttribute("emptyList", new ArrayList<Person>());
		request.setAttribute("nullList", null);
		
		// Initialize the Thymeleaf template engine and process the template
		WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
		templateEngine.process(templateName, ctx, response.getWriter());
	}
	
	private static void initializeTemplateEngine() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(3600000L);
		templateResolver.setCacheable(false);

		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		templateEngine.addDialect(new DataTablesDialect());
	}

}