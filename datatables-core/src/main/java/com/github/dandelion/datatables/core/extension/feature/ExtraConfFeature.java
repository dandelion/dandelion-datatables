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
package com.github.dandelion.datatables.core.extension.feature;

import com.github.dandelion.datatables.core.asset.ExtraConf;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

/**
 * <p>
 * Feature used to merge extra DataTables configuration into the generated one.
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public class ExtraConfFeature extends AbstractExtension {

	@Override
	public String getName() {
		return "extraConf";
	}

	@Override
	public void setup(HtmlTable table) {
		for (ExtraConf conf : table.getTableConfiguration().getExtraConfs()) {
			StringBuilder extaConf = new StringBuilder();
			extaConf.append("$.ajax({url:\"");
			extaConf.append(conf.getSrc());
			extaConf.append("\",dataType: \"text\",type: \"GET\", async: false, success: function(extraProperties, xhr, response) {");
			extaConf.append("$.extend(true, oTable_");
			extaConf.append(table.getId());
			extaConf.append("_params, eval('(' + extraProperties + ')'));");
			extaConf.append("}, error : function(jqXHR, textStatus, errorThrown){");
			extaConf.append("console.log(textStatus);");
			extaConf.append("console.log(errorThrown);");
			extaConf.append("}});");
			extaConf.append("console.log(oTable_" + table.getId() + "_params);");
			appendToBeforeStartDocumentReady(extaConf.toString());
		}
	}
}
