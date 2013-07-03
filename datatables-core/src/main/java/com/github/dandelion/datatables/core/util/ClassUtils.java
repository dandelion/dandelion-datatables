/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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
package com.github.dandelion.datatables.core.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.beanutils.MethodUtils;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;

/**
 * Helper class used for all reflection stuff.
 * 
 * @author Thibault Duchateau
 */
public class ClassUtils {

	/**
	 * <p>
	 * Get a Java class from its name.
	 * 
	 * @param className
	 *            The class name.
	 * @return The corresponding class.
	 * @throws ClassNotFoundException 
	 * @throws BadConfigurationException
	 *             if the class doesn't exist.
	 *             
	 * Tries to load a class with more classloaders. Can be useful in J2EE
	 * applications if jar is loaded from a different classloader than user
	 * classes. If class is not found using the standard classloader, tries whit
	 * the thread classloader.
	 * 
	 * @param className
	 *            class name
	 * @return Class loaded class
	 * @throws ClassNotFoundException
	 *             if none of the ClassLoaders is able to found the reuested
	 *             class
	 */
	public static Class<?> getClass(String className) throws ClassNotFoundException {
		try {
			// trying with the default ClassLoader
			return Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			// trying with thread ClassLoader
			Thread thread = Thread.currentThread();
			ClassLoader threadClassLoader = thread.getContextClassLoader();
			return Class.forName(className, false, threadClassLoader);
		}
	}

	/**
	 * <p>
	 * Instanciate a class.
	 * 
	 * @param klass
	 *            The class to instanciate.
	 * @return a new instance of the given class.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws BadConfigurationException
	 *             if the class is not instanciable.
	 */
	public static Object getNewInstance(Class<?> klass) throws InstantiationException, IllegalAccessException {
		return klass.newInstance();
	}

	/**
	 * <p>
	 * Invoke a method called methodName on the object obj, with arguments args.
	 * 
	 * @param obj
	 *            The object on which to invoke the method.
	 * @param methodName
	 *            The method name to invoke.
	 * @param args
	 *            The potential args used in the method.
	 * @return An object returned by the invoked method.
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws BadConfigurationException
	 *             if the methodName doesn't exist for the given object.
	 */
	public static Object invokeMethod(Object obj, String methodName, Object[] args) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return MethodUtils.invokeMethod(obj, methodName, args);
	}

	/**
	 * <p>
	 * Test if a class exists in the classPath, trying to load it with its name.
	 * 
	 * @param className
	 *            The class to test.
	 * @return true if the class can be used, false otherwise.
	 * @throws BadConfigurationException
	 */
	public static Boolean canBeUsed(String className) {
		Boolean canBeUsed = false;
		try {
			ClassUtils.getClass(className);
			canBeUsed = true;
		} 
		catch (ClassNotFoundException e) {
			// do nothing
		}
		return canBeUsed;
	}

	/**
	 * Determine whether the {@link Class} identified by the supplied name is
	 * present and can be loaded. Will return {@code false} if either the class
	 * or one of its dependencies is not present or cannot be loaded.
	 * 
	 * @param className
	 *            the name of the class to check
	 * @param classLoader
	 *            the class loader to use (may be {@code null}, which indicates
	 *            the default class loader)
	 * @return whether the specified class is present
	 */
	public static boolean isPresent(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (Throwable ex) {
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	public static List<Class<?>> getSubClassesInPackage(String packageName, Class<?> superClass) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<String> dirs = new ArrayList<String>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(URLDecoder.decode(resource.getFile(), "UTF-8"));
		}
		
		TreeSet<String> classes = new TreeSet<String>();
		for (String directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		for (String clazz : classes) {
			Class<?> clazzz = Class.forName(clazz);
			if(superClass.isAssignableFrom(clazzz)){
				classList.add(clazzz);
			}
		}
		return classList;
	}
	
	public static List<Class<?>> getAllClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<String> dirs = new ArrayList<String>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(URLDecoder.decode(resource.getFile(), "UTF-8"));
		}
		TreeSet<String> classes = new TreeSet<String>();
		for (String directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
		for (String clazz : classes) {
			classList.add(Class.forName(clazz));
		}
		return classList;
	}
	
	private static TreeSet<String> findClasses(String path, String packageName) throws MalformedURLException, IOException {
		TreeSet<String> classes = new TreeSet<String>();
		if (path.startsWith("file:") && path.contains("!")) {
			String[] split = path.split("!");
			URL jar = new URL(split[0]);
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry;
			while ((entry = zip.getNextEntry()) != null) {
				if (entry.getName().endsWith(".class")) {
					String className = entry.getName().replaceAll("[$].*", "").replaceAll("[.]class", "").replace('/', '.');
					if (className.startsWith(packageName)) {
						classes.add(className);
					}
				}
			}
		}
		File dir = new File(path);
		if (!dir.exists()) {
			return classes;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				classes.addAll(findClasses(file.getAbsolutePath(), packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				// Build the class name with the package name and the file after
				// removing the .class extension
				String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				classes.add(className);
			}
		}
		return classes;
	}
}