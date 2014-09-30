package com.github.dandelion.datatables.jsp.i18n;

import java.util.Locale;

import javax.servlet.jsp.jstl.core.Config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

public class JstlLocaleResolverTest {

	private MockHttpSession session;
	private MockHttpServletRequest request;
	private Locale fr;
	private Locale en;
	
	@Before
	public void createMainGenerator() {
		fr = new Locale("fr", "FR");
		en = new Locale("en", "US");
		
		session = new MockHttpSession();
		Config.set(session, Config.FMT_LOCALE, fr);
		
		request = new MockHttpServletRequest();
		request.setSession(session);
		Config.set(request, Config.FMT_LOCALE, en);
	}
	
	@Test
	public void should_resolve_locale_from_request_first(){
		JstlLocaleResolver localeResolver = new JstlLocaleResolver();
		Locale locale = localeResolver.resolveLocale(request);
		
		assertThat(locale).isEqualTo(en);
	}
	
	@Test
	public void should_then_resolve_locale_from_session(){
		Config.set(request, Config.FMT_LOCALE, null);
		
		JstlLocaleResolver localeResolver = new JstlLocaleResolver();
		Locale locale = localeResolver.resolveLocale(request);
		
		assertThat(locale).isEqualTo(fr);
	}
}
