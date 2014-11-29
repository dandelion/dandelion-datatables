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
package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.core.utils.StringUtils;
import com.github.dandelion.datatables.core.DatatableBundles;


/**
 * Enumeration containing different types of sort.
 *
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public enum SortType {

	ALT_STRING("alt-string", DatatableBundles.DDL_DT_SORTING_ALT_STRING),
	ANTI_THE("anti-the", DatatableBundles.DDL_DT_SORTING_ANTI_THE),
	CHINESE_STRING("chinese_string", DatatableBundles.DDL_DT_SORTING_CHINESE_STRING),
	DATE_DE("date-de", DatatableBundles.DDL_DT_SORTING_DATE_DE),
	DATE_EU("date-eu", DatatableBundles.DDL_DT_SORTING_DATE_EU),
	DATE_EURO("date-euro", DatatableBundles.DDL_DT_SORTING_DATE_EURO),
	DATE_UK("date-uk", DatatableBundles.DDL_DT_SORTING_DATE_UK),
	FILESIZE("file-size", DatatableBundles.DDL_DT_SORTING_FILESIZE),
	IP_ADDRESS("ip-address", DatatableBundles.DDL_DT_SORTING_IP),
	NATURAL("natural", DatatableBundles.DDL_DT_SORTING_NATURAL),
	PERSIAN("persian", DatatableBundles.DDL_DT_SORTING_PERSIAN),
	SCIENTIFIC("scientific", DatatableBundles.DDL_DT_SORTING_SCIENTIFIC),
	SIGNED_NUM("signed-num", DatatableBundles.DDL_DT_SORTING_SIGNED_NUM),
	TURKISH_STRING("turkish-string", DatatableBundles.DDL_DT_SORTING_TURKISH_STRING);
	
	private String name;
	private DatatableBundles bundle;
	
	private SortType(String name, DatatableBundles bundle){
		this.name = name;
		this.bundle = bundle;
	}

	public String getName() {
		return name;
	}
	
	public DatatableBundles getBundle() {
		return bundle;
	}

	public static SortType findByName(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		for (SortType sortType : values()) {
			if (name.trim().equalsIgnoreCase(sortType.getName())) {
				return sortType;
			}
		}
		return null;
	}
}