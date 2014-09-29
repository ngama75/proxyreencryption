package uvsq.m2secrets.proxyremailserver.dbentities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;

public class DBProxyKey {
	//id
	//owner
    //recipient
	//clé (private byte[] ha1b2;)
	
	public DBProxyKey() {}
	public DBProxyKey(ProxyKey p) {
		//TODO copier les infos
	}
	public ProxyKey getProxyKey() {
		//TODO  copier les infos
		return null;
	}
	
}
