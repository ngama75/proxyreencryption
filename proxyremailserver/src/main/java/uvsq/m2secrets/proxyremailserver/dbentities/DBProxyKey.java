package uvsq.m2secrets.proxyremailserver.dbentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;

@Entity
public class DBProxyKey {
	@Id @GeneratedValue
	private Long id;
	private Long ownerId;
    private Long recipientId;
	private byte[] ha1b2;
	
	public DBProxyKey() {}
	public DBProxyKey(ProxyKey p) {
		id=p.getId();
		ownerId=p.getOwnerId();
		recipientId=p.getRecipientId();
		ha1b2=p.getHa1b2Raw();
	}
	public ProxyKey getProxyKey() {
		ProxyKey reps = new ProxyKey();
		reps.setId(id);
		reps.setOwnerId(ownerId);
		reps.setRecipientId(recipientId);
		reps.setHa1b2Raw(ha1b2);
		return reps;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}
	public byte[] getHa1b2() {
		return ha1b2;
	}
	public void setHa1b2(byte[] ha1b2) {
		this.ha1b2 = ha1b2;
	}
}
