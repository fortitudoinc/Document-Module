package org.openmrs.module.document.api.db;

import java.util.List;
import org.openmrs.api.APIException;
import org.openmrs.module.document.Document;

/**
 * This is the DAO interface. This is never used by the developer, but instead the
 * {@link DocumentService} calls it (and developers call the NoteService)
 */

public interface DocumentDao {
	
	public List<Document> getAllDocuments();
	
	public List<Document> getAllDocumentsForPatient(int patientId);
	
	public Document saveDocument(Document item) throws APIException;
	
}
