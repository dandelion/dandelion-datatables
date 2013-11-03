package com.github.dandelion.datatables.jsp.i18n;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;

import com.github.dandelion.datatables.core.i18n.MessageResolver;
import com.github.dandelion.datatables.jsp.tag.ColumnTag;

public class JstlMessageResolverTest {

	private MockPageContext pageContext;
	private MockHttpServletRequest request;
	
	@Test
	public void should_return_error_message_when_the_bundle_doesnt_exist() throws UnsupportedEncodingException, IOException{
		// Setup
//		InputStream stream = getClass().getClassLoader().getResourceAsStream("com/github/dandelion/datatables/jsp/i18n/datatables_en.properties");
//		ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
		pageContext = new MockPageContext();
		pageContext.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".page", null);
		JstlMessageResolver messageResolver = new JstlMessageResolver(request);

		// Test
		String message = messageResolver.getResource("undefinedKey", "default", new ColumnTag(), pageContext);
		assertThat(message).isEqualTo(MessageResolver.UNDEFINED_KEY + "undefinedKey" + MessageResolver.UNDEFINED_KEY);
	}
	
	@Test
	public void should_return_error_message_when_the_key_is_undefined() throws UnsupportedEncodingException, IOException{
		// Setup
		InputStream stream = getClass().getClassLoader().getResourceAsStream("com/github/dandelion/datatables/jsp/i18n/datatables_en.properties");
		ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
		pageContext = new MockPageContext();
		pageContext.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".page", new LocalizationContext(bundle, new Locale("en", "US")));
		JstlMessageResolver messageResolver = new JstlMessageResolver(request);

		// Test
		String message = messageResolver.getResource("undefinedKey", "default", new ColumnTag(), pageContext);
		assertThat(message).isEqualTo(MessageResolver.UNDEFINED_KEY + "undefinedKey" + MessageResolver.UNDEFINED_KEY);
	}
	
	@Test
	public void should_return_blank_when_the_key_is_defined_but_message_is_blank() throws UnsupportedEncodingException, IOException{
		// Setup
		InputStream stream = getClass().getClassLoader().getResourceAsStream("com/github/dandelion/datatables/jsp/i18n/datatables_en.properties");
		ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
		pageContext = new MockPageContext();
		pageContext.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".page", new LocalizationContext(bundle, new Locale("en", "US")));
		JstlMessageResolver messageResolver = new JstlMessageResolver(request);

		// Test
		String message = messageResolver.getResource("global.blank.value", "default", new ColumnTag(), pageContext);
		assertThat(message).isEqualTo("");
	}
	
	@Test
	public void should_return_the_capitalized_default_message_when_the_key_is_null() throws UnsupportedEncodingException, IOException{
		// Setup
		InputStream stream = getClass().getClassLoader().getResourceAsStream("com/github/dandelion/datatables/jsp/i18n/datatables_en.properties");
		ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
		pageContext = new MockPageContext();
		pageContext.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".page", new LocalizationContext(bundle, new Locale("en", "US")));
		JstlMessageResolver messageResolver = new JstlMessageResolver(request);
		
		// Test
		String message = messageResolver.getResource(null, "default", new ColumnTag(), pageContext);
		assertThat(message).isEqualTo("Default");
	}
	
	@Test
	public void should_return_the_message_when_the_key_is_present_in_the_bundle() throws UnsupportedEncodingException, IOException{
		// Setup
		InputStream stream = getClass().getClassLoader().getResourceAsStream("com/github/dandelion/datatables/jsp/i18n/datatables_en.properties");
		ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
		pageContext = new MockPageContext();
		pageContext.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".page", new LocalizationContext(bundle, new Locale("en", "US")));
		JstlMessageResolver messageResolver = new JstlMessageResolver(request);

		// Test
		String message = messageResolver.getResource("global.msg.info", "default", new ColumnTag(), pageContext);
		assertThat(message).isEqualTo("My infos");
	}
	
	@Test
	public void should_return_the_capitalized_default_message_if_the_key_is_null() throws UnsupportedEncodingException, IOException{
		// Setup
		InputStream stream = getClass().getClassLoader().getResourceAsStream("com/github/dandelion/datatables/jsp/i18n/datatables_en.properties");
		ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
		pageContext = new MockPageContext();
		pageContext.setAttribute(Config.FMT_LOCALIZATION_CONTEXT + ".page", new LocalizationContext(bundle, new Locale("en", "US")));
		JstlMessageResolver messageResolver = new JstlMessageResolver(request);

		// Test
		String message = messageResolver.getResource(null, "default", new ColumnTag(), pageContext);
		assertThat(message).isEqualTo("Default");
	}
}
