package com.github.dandelion.datatables.thymeleaf.util;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.Arguments;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;

import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * Utility methods to manipulate the DOM.
 * 
 * @author Thibault Duchateau
 */
public class DomUtils {

	public static void addScriptTag(Element element, HttpServletRequest request,
			String jsResourceName) {
		Element script = new Element("script");
		script.setAttribute("src", jsResourceName);
		element.getParent().addChild(script);
	}

	public static void addLinkTag(Element element, HttpServletRequest request,
			String cssResourceName) {
		Element link = new Element("link");
		link.setAttribute("href", cssResourceName);
		link.setAttribute("rel", "stylesheet");
		element.getParent().addChild(link);
	}

	public static Element getParentAsElement(Element element) {
		return (Element) element.getParent();
	}

	public static Element getGrandParentAsElement(Element element) {
		return (Element) element.getParent().getParent();
	}

	public static Element getParent(Element element) {
		if (element.hasParent()) {
			return (Element) getParent(element);
		} else {
			return element;
		}
	}

	public static HtmlTable getTable(Arguments arguments) {
		return (HtmlTable) ((IWebContext) arguments.getContext()).getHttpServletRequest()
				.getAttribute("htmlTable");
	}

	/**
	 * Recursively search a node by its type inside a root node.
	 * 
	 * @param nodeClass
	 *            The class to look for.
	 * @return The first node corresponding to the searched class.
	 */
	public static Node getNodeByType(Element root, Class<? extends Node> nodeClass) {

		Node retval = null;

		if (root != null && root.hasChildren()) {
			for (Node node : root.getChildren()) {
				if (node.getClass().equals(nodeClass)) {
					retval = node;
					break;
				}
			}
			if (retval == null) {
				retval = getNodeByType(root.getFirstElementChild(), nodeClass);
			}
		}

		return retval;
	}
}
