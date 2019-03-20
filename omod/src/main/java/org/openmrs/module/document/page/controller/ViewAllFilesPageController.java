/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.document.page.controller;

import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class ViewAllFilesPageController {
	
	public void controller(HttpServletRequest request, @RequestParam(value = "file", required = false) String fileName,
	        PageModel pageModel, PageRequest pageRequest) {
		String folderLocation = OpenmrsUtil.getApplicationDataDirectory() + "/Document";
		ArrayList<String> results = new ArrayList<String>();
		File folder = new File(folderLocation);
		File[] files = folder.listFiles();
		
		//If this pathname does not denote a directory, then listFiles() returns null. 
		if (files != null) {
			for (File file : files) {
				if (file.isFile() && !file.getName().startsWith(".")) {
					results.add(file.getName());
				}
			}
		}
		
		String urlPathToPage = (request.getRequestURL().toString()).trim();
		urlPathToPage = urlPathToPage.substring(0, urlPathToPage.lastIndexOf("/") + 1);
		pageModel.addAttribute("files", results);
		pageModel.addAttribute("url", urlPathToPage);
	}
	
}
