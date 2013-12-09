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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.github.dandelion.datatables.core.configuration.ConfigToken;

public class CollectionUtils {

	public static Object predicateParams;

	public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
		Collection<T> result = new ArrayList<T>();
		for (T element : target) {
			if (predicate.apply(element)) {
				result.add(element);
			}
		}
		return result;
	}

	public static <T> T select(Collection<T> target, Predicate<T> predicate) {
		T result = null;
		for (T element : target) {
			if (!predicate.apply(element))
				continue;
			result = element;
			break;
		}
		return result;
	}

	public static <T> T select(Collection<T> target, Predicate<T> predicate, T defaultValue) {
		T result = defaultValue;
		for (T element : target) {
			if (!predicate.apply(element))
				continue;
			result = element;
			break;
		}
		return result;
	}
	
	public static Boolean hasConfigurationWithValue(Map<ConfigToken<?>, Object> confMap, ConfigToken<?> config, Object value) {

		if(confMap.containsKey(config) && confMap.get(config).equals(value)){
			return true;
		}

		return false;
	}
}