package org.openmrs.module.document.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.document.Document;
import org.openmrs.module.document.api.db.DocumentDao;

/**
 * This is a Hibernate object. It gives us metadata about the currently connected database, the
 * current session, the current db user, etc. To save and get objects, calls should go through
 * sessionFactory.getCurrentSession() <br/>
 * <br/>
 * This is called by Spring. See the /metadata/moduleApplicationContext.xml for the "sessionFactory"
 * setting. See the applicationContext-service.xml file in CORE openmrs for where the actual
 * "sessionFactory" object is first defined.
 * 
 * @param sessionFactory
 */

public class HibernateDocumentDAO implements DocumentDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		
	}
	
	@Override
	public List<Document> getAllDocuments() {
		// FOLLOWING IS AUTO GENERATED SO CHECK CORRECTNESS
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Document.class);
		return crit.list();
	}
	
	@Override
	public List<Document> getAllDocumentsForPatient(int patientId) {
		// FOLLOWING IS AUTO GENERATED SO CHECK CORRECTNESS
		
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Document.class);
		crit.add(Restrictions.eq("patientId", patientId));
		return crit.list();
	}
	
	@Override
	public Document saveDocument(Document item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
}
