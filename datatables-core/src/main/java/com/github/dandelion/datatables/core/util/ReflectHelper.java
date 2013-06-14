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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ClassUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.datatables.core.exception.BadConfigurationException;
import com.github.dandelion.datatables.core.extension.feature.AbstractFeature;
import com.github.dandelion.datatables.core.extension.plugin.AbstractPlugin;

/**
 * Helper class used for all reflection stuff.
 * 
 * @author Thibault Duchateau
 */
public class ReflectHelper {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

	/**
	 * <p>
	 * Get a Java class from its name.
	 * 
	 * @param className
	 *            The class name.
	 * @return The corresponding class.
	 * @throws BadConfigurationException
	 *             if the class doesn't exist.
	 */
	public static Class<?> getClass(String className) throws BadConfigurationException {
		Class<?> klass = null;

		try {
			klass = ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			logger.error("Unable to get class {}", className);
			throw new BadConfigurationException(e);
		}

		return klass;
	}

	/**
	 * <p>
	 * Instanciate a class.
	 * 
	 * @param klass
	 *            The class to instanciate.
	 * @return a new instance of the given class.
	 * @throws BadConfigurationException
	 *             if the class is not instanciable.
	 */
	public static Object getNewInstance(Class<?> klass) throws BadConfigurationException {
		Object retval = null;
		try {
			retval = klass.newInstance();
		} catch (InstantiationException e) {
			logger.error("Unable to get instance of {}", klass);
			throw new BadConfigurationException(e);
		} catch (IllegalAccessException e) {
			logger.error("Unable to get instance of {}", klass);
			throw new BadConfigurationException(e);
		}

		return retval;
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
	 * @throws BadConfigurationException
	 *             if the methodName doesn't exist for the given object.
	 */
	public static Object invokeMethod(Object obj, String methodName, Object[] args)
			throws BadConfigurationException {
		Object retval = null;

		try {
			retval = MethodUtils.invokeMethod(obj, methodName, args);
		} catch (NoSuchMethodException e) {
			logger.error("Unable to invoke method {}", methodName);
			throw new BadConfigurationException(e);
		} catch (IllegalAccessException e) {
			logger.error("Unable to invoke method {}", methodName);
			throw new BadConfigurationException(e);
		} catch (InvocationTargetException e) {
			logger.error("Unable to invoke method {}", methodName);
			throw new BadConfigurationException(e);
		}

		return retval;
	}

	public static String getGetterFromSetter(String setterName){
		return "get" + setterName.substring(setterName.indexOf("set") + 1, setterName.length());
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
	public static Boolean canBeUsed(String className) throws BadConfigurationException {
		Boolean canBeUsed = false;
		try {
			ClassUtils.getClass(className);
			canBeUsed = true;
		} catch (ClassNotFoundException e) {
			logger.warn("Unable to get class {}", className);
			throw new BadConfigurationException(e);
		}
		return canBeUsed;
	}
	
	
	/**
	 * Scan for custom features.
	 * 
	 * @param packageName
	 *            The package name where to scan classes.
	 * @return a list of custom AbstractFeature.
	 * @throws BadConfigurationException
	 */
	public static List<AbstractFeature> scanForFeatures(String packageName) throws BadConfigurationException{
		
		// TODO temporary removed due to a classpath conflict with xml-api, brought by reflections
		// Init return value
		List<AbstractFeature> retval = new ArrayList<AbstractFeature>();
		
		// Init the reflection utility
		Reflections reflections = new Reflections(packageName);
		
		// Scan all subtypes of ActractFeature
		Set<Class<? extends AbstractFeature>> subTypes = reflections.getSubTypesOf(AbstractFeature.class);
		
		// Instanciate all found classes
		for(Class<? extends AbstractFeature> clazz : subTypes){
			
			try {
				retval.add((AbstractFeature) ClassUtils.getClass(clazz.getName()).newInstance());
			} catch (ClassNotFoundException e) {
				logger.warn("Unable to get class {}", clazz.getName());
				throw new BadConfigurationException(e);
			} catch (InstantiationException e) {
				logger.warn("Unable to instanciate class {}", clazz.getName());
				throw new BadConfigurationException(e);
			} catch (IllegalAccessException e) {
				logger.warn("Unable to access the class {}", clazz.getName());
				throw new BadConfigurationException(e);
			}
		}
		
		return retval;
	}
	
	/**
	 * Scan for custom plugins.
	 * 
	 * @param packageName
	 *            The package name where to scan classes.
	 * @return a list of custom AbstractPlugin.
	 * @throws BadConfigurationException
	 */
	public static List<AbstractPlugin> scanForPlugins(String packageName) throws BadConfigurationException{
		
		// TODO temporary removed due to a classpath conflict with xml-api, brought by reflections
		
		// Init return value
		List<AbstractPlugin> retval = new ArrayList<AbstractPlugin>();
		
		// Init the reflection utility
		Reflections reflections = new Reflections(packageName);
		
		// Scan all subtypes of ActractFeature
		Set<Class<? extends AbstractPlugin>> subTypes = reflections.getSubTypesOf(AbstractPlugin.class);
		
		// Instanciate all found classes
		for(Class<? extends AbstractPlugin> clazz : subTypes){
			
			try {
				retval.add((AbstractPlugin) ClassUtils.getClass(clazz.getName()).newInstance());
			} catch (ClassNotFoundException e) {
				logger.warn("Unable to get class {}", clazz.getName());
				throw new BadConfigurationException(e);
			} catch (InstantiationException e) {
				logger.warn("Unable to instanciate class {}", clazz.getName());
				throw new BadConfigurationException(e);
			} catch (IllegalAccessException e) {
				logger.warn("Unable to access the class {}", clazz.getName());
				throw new BadConfigurationException(e);
			}
		}
		
		return retval;
	}
	
	
	/**
     * Tries to load a class with more classloaders. Can be useful in J2EE applications if jar is loaded from a
     * different classloader than user classes. If class is not found using the standard classloader, tries whit the
     * thread classloader.
     * @param className class name
     * @return Class loaded class
     * @throws ClassNotFoundException if none of the ClassLoaders is able to found the reuested class
     */
    public static Class< ? > classForName(String className) throws ClassNotFoundException
    {
        try
        {
            // trying with the default ClassLoader
            return Class.forName(className);
        }
        catch (ClassNotFoundException cnfe)
        {
            try
            {
                // trying with thread ClassLoader
                Thread thread = Thread.currentThread();
                ClassLoader threadClassLoader = thread.getContextClassLoader();
                return Class.forName(className, false, threadClassLoader);
            }
            catch (ClassNotFoundException cnfe2)
            {
                throw cnfe2;
            }
        }
    }

}