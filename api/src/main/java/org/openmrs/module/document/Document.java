package org.openmrs.module.document;

import java.io.Serializable;
import java.util.Date;
import org.openmrs.BaseOpenmrsData;

public class Document extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id, patientId;
	
	private int userId; // user who uploaded doc
	
	private String description;
	
	private String serverFileName; // full, stored, file name of document
	
	private String originalFileName; // file name of uploaded file on user's machine
	
	// predefined types are added to global properties; e.g. xray, radiology, patient image, lab results
	private String docType;
	
	private Date dateCreated;
	
	private int isVoided;
	
	private String void_reason;
	
	public int getIsVoided() {
		return isVoided;
	}
	
	public void setIsVoided(int isVoided) {
		this.isVoided = isVoided;
	}
	
	public String getVoid_reason() {
		return void_reason;
	}
	
	public void setVoid_reason(String void_reason) {
		this.void_reason = void_reason;
	}
	
	public String getOriginalFileName() {
		return originalFileName;
	}
	
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getServerFileName() {
		return serverFileName;
	}
	
	public void setServerFileName(String serverFileName) {
		this.serverFileName = serverFileName;
	}
	
	public String getDocType() {
		return docType;
	}
	
	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * The primary key for this Document. If this is null, the database will generate the integer
	 * primary key because we marked the Document.id column to auto_increment. <br/>
	 * <br/>
	 * 
	 * @see org.openmrs.OpenmrsObject#setId(java.lang.Integer)
	 */
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
}
