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
package com.github.dandelion.datatables.core.asset;

import com.github.dandelion.datatables.core.extension.plugin.ScrollerPlugin;

/**
 * <p>
 * A parameter can be seen as a name/value pair in the DataTables
 * configuration Javascript object.
 * <p>
 * Some extension may need to modify the DataTables parameters object, that's
 * why there's a Mode attribute, which defines the way the configuration will
 * affect this object.
 * <p>
 * For example, in order to use the {@link ScrollerPlugin}, you need to modify
 * the <tt>sDom</tt> parameter, adding a <tt>S</tt> to the default value (which
 * is <tt>lfrtip</tt>). That's why in the <code>setup</code> method, you will
 * find among others :
 * 
 * <pre>
 * addParameter(new Configuration(DTConstants.DT_DOM, &quot;S&quot;, Configuration.Mode.APPEND));
 * </pre>
 * <p>
 * So the final value of the <tt>sDom</tt> parameter will be <tt>lfrtipS</tt>
 * 
 * @author Thibault Duchateau
 * @since 0.5.0
 */
public class Parameter {

	/**
	 * DataTables parameter's name
	 */
	private String name;

	/**
	 * DataTables parameter's value
	 */
	private Object value;

	/**
	 * DataTables parameter's updating mode
	 */
	private Mode mode;

	public enum Mode {
		// Override the existing parameter with the new one
		OVERRIDE,

		// Append the new parameter to the old one
		APPEND,

		// Prepend the new parameter to the old one
		PREPEND,

		// Append the new parameter to the old one, with a separating space
		APPEND_WITH_SPACE,

		// Prepend the new parameter to the old one, with a separating space
		PREPEND_WITH_SPACE
	}

	public Parameter(String name){
		this(name, null, Mode.OVERRIDE);
	}
	
	public Parameter(String name, Object value) {
		this(name, value, Mode.OVERRIDE);
	}

	public Parameter(String name, Object value, Parameter.Mode mode) {
		this.name = name;
		this.value = value;
		this.setMode(mode);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
}