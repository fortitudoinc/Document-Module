/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.document.web.controller;

import org.apache.commons.io.IOUtils;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class configured as controller using annotation and mapped with the URL of
 * 'module/document/documentLink.form'.
 */
@Controller("${rootrootArtifactId}.DocumentController")
@RequestMapping(value = "document/document.form")
public class DocumentController {
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void onGet(@RequestParam(value = "file") String fileName, HttpServletResponse response) throws IOException {
		System.out.println("DocumentController: onGet " + fileName);
		try {
			String folderLocation = OpenmrsUtil.getApplicationDataDirectory() + "/Document";
			File folder = new File(folderLocation);
			
			File file = new File(folder, fileName);
			
			InputStream is = new FileInputStream(file);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		}
		catch (IOException e) {
			response.setStatus(404);
		}
	}
	
}
