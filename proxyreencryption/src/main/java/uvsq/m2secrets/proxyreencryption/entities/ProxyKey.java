package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;
import uvsq.m2secrets.proxyreencryption.utils.SerializableObject;

@SuppressWarnings("serial")
public class ProxyKey extends SerializableObject {
	private Long id;
	private Long ownerId;
    private Long recipientId;
	private byte[] ha1b2;
	
	public ProxyKey() {}
	
	public static ProxyKey delegate(User owner, User recipient, PrivKey ownerPriv) {
	   ProxyKey reps = new ProxyKey();
	   PubKey recvPubkey = recipient.getPubKey();
	   Element ha1b2 = recvPubkey.getHa2().duplicate().powZn(ownerPriv.getA1());
	   reps.setOwnerId(owner.getId());
	   reps.setRecipientId(recipient.getId());
	   reps.setHa1b2(ha1b2);
	   return reps;
	}
	public static EncryptedDocument reencrypt(ProxyKey proxy, EncryptedDocument edoc) {
		if (edoc.getRecipientId()!=proxy.ownerId) {
			System.err.println("The recipient of the document is not me");
			return null;
		}
		EncryptedSessionKey esk = edoc.getEncryptedSessionKey();
		if (esk.getLevel()!=2) {
			System.err.println("Can only reencrypt a level 2 ciphertext");
			return null;
		}
		Element za1b2k = Parameters.pairing().pairing(proxy.getHa1b2(),esk.getGk());
		EncryptedSessionKey newesk = new EncryptedSessionKey();
		newesk.setLevel3(za1b2k,esk.getMza1k());
		EncryptedDocument newedoc = new EncryptedDocument();
		newedoc.setId(edoc.getId());
		newedoc.setRecipientId(proxy.recipientId);
		newedoc.setEncryptedSessionKey(newesk);
		newedoc.setEncryptedBody(edoc.getEncryptedBody());
		return newedoc;
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

	public Element getHa1b2() {
		return Parameters.G2().newElementFromBytes(ha1b2);
	}

	public void setHa1b2(Element ha1b2) {
		this.ha1b2 = ha1b2.toBytes();
	}

	public byte[] getHa1b2Raw() {
		return ha1b2;
	}

	public void setHa1b2Raw(byte[] ha1b2) {
		this.ha1b2=ha1b2;		
	}
		
}
