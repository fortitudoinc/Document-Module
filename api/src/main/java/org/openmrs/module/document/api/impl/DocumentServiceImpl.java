package org.openmrs.module.document.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.document.Document;
import org.openmrs.module.document.api.DocumentService;
import org.openmrs.module.document.api.db.DocumentDao;
import org.springframework.transaction.annotation.Transactional;

public class DocumentServiceImpl extends BaseOpenmrsService implements DocumentService {
	
	DocumentDao dao;
	
	private static final Log log = LogFactory.getLog(DocumentServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(DocumentDao dao) {
		this.dao = dao;
	}
	
	public DocumentDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional(readOnly = true)
	public List<Document> getAllDocuments() {
		return dao.getAllDocuments();
	}
	
	@Transactional(readOnly = true)
	public List<Document> getAllDocumentsForPatient(int patientId) {
		return dao.getAllDocumentsForPatient(patientId);
	}
	
	@Transactional
	public Document saveDocument(Document item) throws APIException {
		return dao.saveDocument(item);
	}
	
}
