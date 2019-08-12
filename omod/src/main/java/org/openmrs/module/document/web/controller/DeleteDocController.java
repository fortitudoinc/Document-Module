/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.document.Document;
import org.openmrs.module.document.api.DocumentService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;

/**
 * @author levine
 */
@Controller("${rootrootArtifactId}.DeleteDocController")
@RequestMapping(value = "document/delete.form")
public class DeleteDocController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public void onGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "This method is not allowed.");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String onPost(HttpServletRequest request, @RequestParam("docId") int docId,
	        @RequestParam("patientId") int patientId, HttpServletResponse response) {
		
		System.out.println("DeleteDocController: onPost  docId: " + docId + "patientId: " + patientId);
		Document doc = null;
		JSONObject jsonObject = new JSONObject();
		try {
			List<Document> patientDocs = Context.getService(DocumentService.class).getAllDocumentsForPatient(patientId);
			for (Document patientDoc : patientDocs) {
				if (patientDoc.getId() == docId) {
					doc = patientDoc;
					break;
				}
			}
			
			if (doc == null) {
				jsonObject.put("result", "failure - did not find file");
				return jsonObject.toString();
			}
			
			String folderLocation = OpenmrsUtil.getApplicationDataDirectory() + "/Document";
			String filePath = folderLocation + "/" + doc.getServerFileName();
			System.out.println("DELETING PATIENT FILE: " + filePath);
			File file = new File(filePath);
			file.delete();
			
			doc.setIsVoided(1);
			doc.setVoid_reason("voided reason");
			Context.getService(DocumentService.class).saveDocument(doc);
			
			jsonObject.put("result", "success");
			/*
			jsonObject.put("originalFileName", fileName);
			jsonObject.put("serverFileName", file.getName());
			jsonObject.put("docType", docType);
			jsonObject.put("docDescription", docDescription);
			jsonObject.put("docId", savedDoc.getId());
			jsonObject.put("file-location", file.getAbsolutePath());
			 */
			HttpSession session = request.getSession();
			InfoErrorMessageUtil.flashInfoMessage(session, "Document Deleted");
			
		}
		catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("result", "failed");
		}
		return jsonObject.toString();
	}
	
}
