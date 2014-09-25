package uvsq.m2secrets.proxyreencryption.entities;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class User implements Serializable {
	private Long id;
	private String name;
	private byte[] pubKey;
	
	public User() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PubKey getPubKey() {
		return new PubKey(pubKey);
	}
	public byte[] getPubKeyRaw() {
		return pubKey;
	}

	public void setPubKey(PubKey pubKey) {
		this.pubKey = pubKey.toBytes();
	}

	public void setPubKeyRaw(byte[] pubKey) {
		this.pubKey = pubKey;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", pubKey=");
		builder.append(pubKey != null ? Arrays.toString(Arrays.copyOf(pubKey,
				Math.min(pubKey.length, maxLen))) : null);
		builder.append("]");
		return builder.toString();
	}	

}
