package uvsq.m2secrets.proxyremailserver.dbentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;

@Entity
public class DBEncryptedDocument  {
	@Id @GeneratedValue
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

	public byte[] getEncryptedSessionKey() {
		return encryptedSessionKey;
	}

	public void setEncryptedSessionKey(byte[] encryptedSessionKey) {
		this.encryptedSessionKey = encryptedSessionKey;
	}

	public byte[] getEncryptedBody() {
		return encryptedBody;
	}

	public void setEncryptedBody(byte[] encryptedBody) {
		this.encryptedBody = encryptedBody;
	}
}
