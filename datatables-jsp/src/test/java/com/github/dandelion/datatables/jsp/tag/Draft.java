package com.github.dandelion.datatables.jsp.tag;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Test;

import com.github.dandelion.datatables.core.model.JsResource;
import com.github.dandelion.datatables.jsp.DomPhantomJsTest;

public class Draft extends DomPhantomJsTest {

//	public void accessAsset(String URI) throws IOException, Exception {
//
//		if (URI.contains("datatables")) {
//
//			
//			System.out.println("URI = " + URI);
//			String asset = URI.substring(URI.lastIndexOf("/") + 1);
//			System.out.println("asset = " + asset);
//
//			
//			JsResource attr = (JsResource) context.getServletContext().getAttribute(asset);
//			System.out.println("attr = " + attr);
//			System.out.println("attr = " + attr.getContent());
//
//			
//			this.request.setURI("/datatablesController/" + asset);
//			this.response.parse(servletTester.getResponses(request.generate()));
//			System.out.println("request.getURI() = " + request.getURI());
//			System.out.println("request.generate() = " + request.generate());
//			
//			System.out.println("response = " + response.toString());
//			System.out.println("status = " + response.getStatus());
//			System.out.println("content = " + response.getContent());
//			System.out.println("reason = " + response.getReason());
//			System.out.println("contentType = " + response.getContentType());
//			System.out.println("CharacterEncoding = " + response.getCharacterEncoding());
//			System.out.println("bytes = " + response.getContentBytes());
//			System.out.println("URI = " + response.getURI());
//
//			System.out.println(servletTester.getContext().getAttributes().toString());
//			System.out.println("test recup = " + servletTester.getContext().getAttribute(asset));
//		}
//	}
//
//	@Test
//	public void test1() throws IOException, Exception {
//		goTo("/basicFeatures/table_disable_paging.jsp");
//
//		FluentList<FluentWebElement> list = find("script");
//		for (FluentWebElement webElement : list) {
//			System.out.println(webElement);
//			System.out.println("src = " + webElement.getAttribute("src"));
//
//			accessAsset(webElement.getAttribute("src"));
//		}
//	}
//	
//	@Test
//	public void test2() throws IOException, Exception {
//		goTo("/basicFeatures/table_disable_paging.jsp");
//
//		FluentList<FluentWebElement> list = find("script");
//		for (FluentWebElement webElement : list) {
//			System.out.println(webElement);
//			System.out.println("src = " + webElement.getAttribute("src"));
//
//			if (webElement.getAttribute("src").contains("datatables-")) {
//				
////				String mainKey = request.getParameter("c") + "|" + request.getParameter("id");
//				URI uri=URI.create(webElement.getAttribute("src"));
//				System.out.println("URI = " + uri);
//				
//				URL url2 = new URL(webElement.getAttribute("src"));
////				uri.getgetQueryParameter("para1");
//				
//				URL url = new URL(webElement.getAttribute("src"));
//
//				String source = "";
//				URLConnection yc;
//				try {
//					yc = url.openConnection();
//					BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//					String inputLine;
//					while ((inputLine = in.readLine()) != null){
////						System.out.println(inputLine);
//						source += inputLine;
//					}
//					in.close();					
//					
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				
//				System.out.println("Source = " + source);
//				
//				ScriptEngineManager mgr = new ScriptEngineManager();
//				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
//				try {
//					jsEngine.eval(new FileReader("jquery-1.9.1.min.js"));
//					jsEngine.eval(source);
////				    ScriptValueConverter.unwrapValue(value)
//				} catch (ScriptException ex) {
//				      ex.printStackTrace();
//				}
//			}
//		}
//	}
//	
//	@Test
//	public void test3() throws IOException, Exception {
//		goTo("/basicFeatures/table_disable_paging.jsp");
//		
////		String mainKey = request.getParameter("c") + "|" + request.getParameter("id");
//	}
}
