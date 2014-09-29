package uvsq.m2secrets.proxyremailserver.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyreencryption.entities.User;

@Remote
public interface MailService {
    //public functions
	public User findUser(String name);
	public User findUser(Long uid);
	public Map<Long, String> getUserNames();
	public Long postEncryptedDocument(EncryptedDocument edoc);
	
	//authenticated functions
	public EncryptedDocument askLogin(Long my_uid);
	public User validateLogin(byte[] answer);
	
	public List<EncryptedDocument> myEncryptedDocuments();
	
	public Long postProxy(ProxyKey p);
	public List<ProxyKey> myIncomingProxys();
	public List<ProxyKey> myOutgoingProxys();
	
	public boolean revokeProxy(Long id);
}
