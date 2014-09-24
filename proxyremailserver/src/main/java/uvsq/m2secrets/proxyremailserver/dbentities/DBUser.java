package uvsq.m2secrets.proxyremailserver.dbentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import uvsq.m2secrets.proxyreencryption.entities.User;

@Entity
public class DBUser {
	@Id @GeneratedValue
	private Long id;
	private String name;
	private byte[] pubKey;
	
	public DBUser() {}
	public DBUser(User u) {
		super();
		name=u.getName();
		pubKey=u.getPubKeyRaw();
		id=u.getId();
	}
	public User getUser() {
		User reps = new User();
		reps.setId(id);
		reps.setName(name);
		if (pubKey!=null)
		reps.setPubKeyRaw(pubKey);
		return reps;
	}

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

	public byte[] getPubKey() {
		return pubKey;
	}

	public void setPubKey(byte[] pubKey) {
		this.pubKey = pubKey;
	}
	
}
