/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.document.page.controller;

import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.document.Document;
import org.openmrs.module.document.api.DocumentService;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class ViewUploadFilesPageController {
	
	public void controller(HttpServletRequest request, @RequestParam(value = "file", required = false) String fileName,
	        @RequestParam(value = "patientId", required = false) Patient patient, PageModel pageModel,
	        PageRequest pageRequest) {
		ArrayList<Document> allDocs, docs = new ArrayList<Document>();
		allDocs = (ArrayList<Document>) Context.getService(DocumentService.class).getAllDocumentsForPatient(
		    patient.getPatientId());
		/*
		String folderLocation = OpenmrsUtil.getApplicationDataDirectory() + "/Document";
		ArrayList<String> results = new ArrayList<String>();
		File folder = new File(folderLocation);
		File[] files = folder.listFiles();
		
		String identifier = patient.getPatientIdentifier().getIdentifier();
		
		//If this pathname does not denote a directory, then listFiles() returns null. 
		if (files != null) {
		for (File file : files) {
			if (file.isFile() && file.getName().startsWith(identifier)) {
				results.add(file.getName());
			}
		}
		}
		 */
		for (Document doc : allDocs) {
			if (doc.getIsVoided() == 0) {
				docs.add(doc);
				//System.out.println("ViewUploadFilesPageController, doc added: " + doc.getOriginalFileName());
			} else {
				//System.out.println("****DOC voided: " + doc.getId());
			}
		}
		
		String types = Context.getAdministrationService().getGlobalProperty("document.Doctypes");
		String[] typeArray;
		if (types == null) {
			typeArray = "xray,derm".split(",");
			//System.out.println("NO DOCUMENT TYPES IN GLOBAL PROPERTIES");
		} else {
			//System.out.println("*****DOCUMENT TYPES: " + types);
			typeArray = types.split(",");
		}
		String urlPathToPage = (request.getRequestURL().toString()).trim();
		urlPathToPage = urlPathToPage.substring(0, urlPathToPage.lastIndexOf("/") + 1);
		pageModel.addAttribute("docs", docs);
		pageModel.addAttribute("types", typeArray);
		pageModel.addAttribute("url", urlPathToPage);
	}
	
}
