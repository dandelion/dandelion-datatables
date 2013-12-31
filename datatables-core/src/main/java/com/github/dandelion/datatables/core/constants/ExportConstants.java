/*
 * [The "BSD licence"]
 * Copyright (c) 2013 Dandelion
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
package com.github.dandelion.datatables.core.constants;

/**
 * Dandelion-datatables export related constants.
 * 
 * @author Thibault Duchateau
 */
public class ExportConstants {

	/** Request attributes */

	// Export properties
	public static final String DDL_DT_REQUESTATTR_EXPORT_CONF = "ddl-dt-export-conf";

	// Export content
	public static final String DDL_DT_REQUESTATTR_EXPORT_CONTENT = "ddl-dt-export-content";

	/** Request parameters */

	// Table is being exported
	public static final String DDL_DT_REQUESTPARAM_EXPORT_IN_PROGRESS = "dtp";
	
	// Export type (filter vs controller)
	public static final String DDL_DT_REQUESTPARAM_EXPORT_TYPE = "dtt";
	
	// Table id
	public static final String DDL_DT_REQUESTPARAM_EXPORT_ID = "dti";
		
	// Type of current export
	public static final String DDL_DT_REQUESTPARAM_EXPORT_FORMAT = "dtf";
}