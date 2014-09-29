package uvsq.m2secrets.proxyremailserver.dbentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;

//TODO: transformer en entité JPA
public class DBEncryptedDocument  {
	private Long id;
	private Long recipientId;
	private byte[] encryptedSessionKey;
	private byte[] encryptedBody;

	public DBEncryptedDocument() {}
	public DBEncryptedDocument(EncryptedDocument edoc) {
		id = edoc.getId();
		recipientId = edoc.getRecipientId();
		encryptedSessionKey = edoc.getEncryptedSessionKeyRaw();
		encryptedBody = edoc.getEncryptedBody();
	}
	
	public EncryptedDocument getEncryptedDocument() {
		EncryptedDocument reps = new EncryptedDocument();
		reps.setId(id);
		reps.setRecipientId(recipientId);
		reps.setEncryptedSessionKeyRaw(encryptedSessionKey);
		reps.setEncryptedBody(encryptedBody);
		return reps;
	}
	
	//TODO: ne pas oublier les getters and setters

}
