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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import org.openmrs.Patient;

/**
 * This class configured as controller using annotation and mapped with the URL of
 * 'module/document/documentLink.form'.
 */
@Controller("${rootrootArtifactId}.UploadController")
@RequestMapping(value = "document/upload.form")
public class UploadController {
	
	/**
	 * Logger for this class and subclasses
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void onGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "This method is not allowed.");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String onPost(HttpServletRequest request, @RequestParam("file") String fileDataUrl,
	        @RequestParam("filename") String fileName, @RequestParam(value = "patientId", required = false) Patient patient,
	        HttpServletResponse response) {
		
		System.out.println("UploadController: onPost  fileName: " + fileName + " Patient: " + patient.getGivenName() + " "
		        + patient.getFamilyName());
		
		JSONObject jsonObject = new JSONObject();
		try {
			File file = getFile(fileDataUrl, fileName, patient);
			jsonObject.put("result", "success");
			jsonObject.put("filename", fileName);
			jsonObject.put("fullFilename", file.getName());
			jsonObject.put("file-location", file.getAbsolutePath());
		}
		catch (IOException e) {
			e.printStackTrace();
			jsonObject.put("result", "failed");
		}
		return jsonObject.toString();
	}
	
	private static File getFile(String fileDataUrl, String fileName, Patient patient) throws IOException {
		String identifier = patient.getPatientIdentifier().getIdentifier();
		String folderLocation = OpenmrsUtil.getApplicationDataDirectory() + "/Document";
		File folder = new File(folderLocation);
		folder.mkdirs();
		
		String fname, ext, f = fileName;
		int i, indexOfExtension = f.lastIndexOf(".");
		fname = f.substring(0, indexOfExtension) + "-";
		ext = f.substring(indexOfExtension);
		String date = (new Date()).toString();
		date = date.replaceAll("\\s", "-");
		date = date.replaceAll(":", ".");
		fileName = identifier + "." + fname + date + ext;
		
		File file = new File(folder, fileName);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
		fileDataUrl = fileDataUrl.substring(fileDataUrl.indexOf(",") + 1);
		stream.write(Base64.decodeBase64(fileDataUrl.getBytes()));
		stream.close();
		
		return file;
	}
	
}
