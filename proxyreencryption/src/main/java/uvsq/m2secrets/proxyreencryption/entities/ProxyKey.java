package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class ProxyKey implements Serializable {
	private Long id;
	private Long ownerId;
    private Long recipientId;
	private byte[] ha1b2;
	
	public ProxyKey() {}
	
	public static ProxyKey delegate(User owner, User recipient, PrivKey ownerPriv) {
		//TODO: create the proxy delegation
		return null;
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
		//TODO: reencrypt the document
		return null;
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

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("ProxyKey [id=");
		builder.append(id);
		builder.append(", ownerId=");
		builder.append(ownerId);
		builder.append(", recipientId=");
		builder.append(recipientId);
		builder.append(", ha1b2=");
		builder.append(ha1b2 != null ? Arrays.toString(Arrays.copyOf(ha1b2,
				Math.min(ha1b2.length, maxLen))) : null);
		builder.append("]");
		return builder.toString();
	}
	
		
}
