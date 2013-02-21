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
package com.github.dandelion.datatables.core.export;

import java.util.HashMap;
import java.util.Map;

/**
 * All the available export types.
 *
 * @author Thibault Duchateau
 */
public enum ExportType {
	
	CSV("csv", "text/csv", 1), 
	HTML("html", "text/html", 2), 
	XML("xml", "text/xml", 3), 
	RTF("rtf", "text/rtf", 4),
	PDF("pdf", "application/pdf", 5), 
	XLS("xls", "application/vnd.ms-excel", 6),
	JSON("json", "", 7),
	XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 8);
	
	private String extension;
	private String mimeType;
	private Integer urlParameter;
	
	private ExportType(String extension, String mimeType, Integer urlParameter){
		this.extension = extension;
		this.mimeType = mimeType;
		this.urlParameter = urlParameter;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Integer getUrlParameter() {
		return urlParameter;
	}

	public void setUrlParameter(Integer urlParameter) {
		this.urlParameter = urlParameter;
	}
	
	private static final Map<Integer,ExportType> map;
    static {
        map = new HashMap<Integer,ExportType>();
        for (ExportType v : ExportType.values()) {
            map.put(v.getUrlParameter(), v);
        }
    }
    
    public static ExportType findByUrlParameter(Integer i) {
        return map.get(i);
    }
}