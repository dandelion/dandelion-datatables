package com.github.dandelion.datatables.jsp.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.jetty.testing.HttpTester;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;

import com.github.dandelion.datatables.jsp.DomPhantomJsTest;

public class Draft extends DomPhantomJsTest {

	public void test(String URI) throws IOException, Exception{

		String test = URI.substring(URI.lastIndexOf("/") + 1);
		System.out.println("test = " + test);
		
		System.out.println("URI = " + URI);
		HttpTester request = new HttpTester();
		request.setMethod("GET");
		request.setURI(URI);

		HttpTester response = new HttpTester();
		response.parse(servletTester.getResponses(request.generate()));
		System.out.println("response = " + response);
		System.out.println("content = " + response.getContent());
	}
	
	public void draft() throws IOException, Exception{
		FluentList<FluentWebElement> list = find("script");
		for (FluentWebElement webElement : list) {
			System.out.println(webElement);
			System.out.println("src = " + webElement.getAttribute("src"));
			
			test(webElement.getAttribute("src"));
			
			URL url = new URL(webElement.getAttribute("src"));
			String charset = "UTF-8";
			
			URLConnection yc;
			try {
				yc = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					System.out.println(inputLine);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
