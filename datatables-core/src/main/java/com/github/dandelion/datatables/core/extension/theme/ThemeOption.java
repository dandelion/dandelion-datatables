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
package com.github.dandelion.datatables.core.extension.theme;

import com.github.dandelion.datatables.core.constants.CdnConstants;

/**
 * All possible theme options.
 * 
 * @author Thibault Duchateau
 */
public enum ThemeOption {

	// jQuery option
	BASE(CdnConstants.CDN_JQUERYUI_THEME_BASE_CSS),
	BLACKTIE(CdnConstants.CDN_JQUERYUI_THEME_BLACKTIE_CSS),
	BLITZER(CdnConstants.CDN_JQUERYUI_THEME_BLITZER_CSS),
	CUPERTINO(CdnConstants.CDN_JQUERYUI_THEME_CUPERTINO_CSS),
	DARKHIVE(CdnConstants.CDN_JQUERYUI_THEME_DARKHIVE_CSS),
	DOTLUV(CdnConstants.CDN_JQUERYUI_THEME_DOTLUV_CSS),
	EGGPLANT(CdnConstants.CDN_JQUERYUI_THEME_EGGPLANT_CSS),
	EXCITEBIKE(CdnConstants.CDN_JQUERYUI_THEME_EXCITEBIKE_CSS),
	FLICK(CdnConstants.CDN_JQUERYUI_THEME_FLICK_CSS),
	HOTSNEAKS(CdnConstants.CDN_JQUERYUI_THEME_HOTSNEAKS_CSS),
	HUMANITY(CdnConstants.CDN_JQUERYUI_THEME_HUMANITY_CSS),
	LEFROG(CdnConstants.CDN_JQUERYUI_THEME_LEFROG_CSS),
	MINTCHOC(CdnConstants.CDN_JQUERYUI_THEME_MINTCHOC_CSS),
	OVERCAST(CdnConstants.CDN_JQUERYUI_THEME_OVERCAST_CSS),
	PEPPERGRINDER(CdnConstants.CDN_JQUERYUI_THEME_PEPPERGRINDER_CSS),
	REDMOND(CdnConstants.CDN_JQUERYUI_THEME_REDMOND_CSS),
	SMOOTHNESS(CdnConstants.CDN_JQUERYUI_THEME_SMOOTHNESS_CSS),
	SOUTHSTREET(CdnConstants.CDN_JQUERYUI_THEME_SOUTHSTREET_CSS),
	START(CdnConstants.CDN_JQUERYUI_THEME_START_CSS),
	SUNNY(CdnConstants.CDN_JQUERYUI_THEME_SUNNY_CSS),
	SWANKYPURSE(CdnConstants.CDN_JQUERYUI_THEME_SWANKYPURSE_CSS),
	TRONTASTIC(CdnConstants.CDN_JQUERYUI_THEME_TRONTASTIC_CSS),
	UIDARKNESS(CdnConstants.CDN_JQUERYUI_THEME_UIDARKNESS_CSS),
	UILIGHTNESS(CdnConstants.CDN_JQUERYUI_THEME_UILIGHTNESS_CSS),
	VADER(CdnConstants.CDN_JQUERYUI_THEME_VADER_CSS),
	
	// Bootstrap options
	TABLECLOTH("");
	
	private String cssSource;

	private ThemeOption(String cssSource){
		this.cssSource = cssSource;
	}
	
	public String getCssSource() {
		return cssSource;
	}

	public void setCssSource(String cssSource) {
		this.cssSource = cssSource;
	}
}