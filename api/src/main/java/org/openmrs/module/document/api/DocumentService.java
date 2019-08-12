package org.openmrs.module.document.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.document.Document;
import org.openmrs.module.document.Document;
import org.openmrs.module.document.DocumentConfig;
import org.openmrs.module.document.DocumentConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DocumentService extends OpenmrsService {
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<Document> getAllDocuments();
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<Document> getAllDocumentsForPatient(int patientId);
	
	@Authorized(DocumentConfig.MODULE_PRIVILEGE)
	@Transactional
	public Document saveDocument(Document item) throws APIException;
	
}
