package uvsq.m2secrets.proxyremailserver.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyreencryption.entities.EncryptedSessionKey;
import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.dao.MainDao;

@Stateful
public class MailServiceImpl implements MailService {
    /**
     * If no user is logged-in, this field shall be null  
     * Else, this field should contain the id of the logged-in user
     */
	private Long my_uid;
    
	/**
	 * During the login-procedure, a random challenge will be generated
	 */
    private byte[] challenge;
    private Long requested_uid;
	
    @EJB
    private MainDao dao;
	
    public MailServiceImpl() {
		my_uid = null;
	}
     
	@Override
	public User findUser(String name) {
		return dao.getUser(name);
	}

	@Override
	public User findUser(Long uid) {
		return dao.getUser(uid);
	}

	/**
	 * this function is called the first time a user tries to log-in
	 * It should generate a random challenge, and encrypt it with the
	 * public key of the user who wishes to log-in
	 */
	@Override
	public EncryptedDocument askLogin(Long uid) {
		User u = findUser(uid);
		if (u==null) return null;
		requested_uid = uid;
		my_uid = null;
		challenge = new byte[256];
		new Random().nextBytes(challenge);
		EncryptedDocument echal 
		= EncryptedDocument.encrypt(challenge, u, 1);
		return echal;
	}

	/**
	 * Once the user has decrypted the challenge, the
	 * server compares it with the original challenge
	 * If it matches, then the login is valid.
	 */
	@Override
	public User validateLogin(byte[] answer) {
		if (!Arrays.equals(answer, challenge)) return null;
		my_uid=requested_uid;
		return dao.getUser(my_uid);		
	}

	@Override
	public Long postEncryptedDocument(EncryptedDocument edoc) {
		if (edoc.getId()!=null) return null;
		return dao.insertOrUpdate(edoc);
	}

	/**
	 * This function should return all messages which the logged-in user 
	 * may decrypt:
	 *  - messages whose recipient is the user
	 *  - all messages level2 encrypted whose recipient has set a 
	 *    reencryption key. In this case, it will silently 
	 *    reencrypt it
	 */
	@Override
	public List<EncryptedDocument> myEncryptedDocuments() {
		if (my_uid==null) return null;
		List<EncryptedDocument> x = dao.getAllEncryptedDocuments();
		List<EncryptedDocument> reps = new ArrayList<>();
		List<ProxyKey> proxys = dao.getAllProxyKeys();
		Map<Long, ProxyKey> my_proxys = new HashMap<>();
		for (ProxyKey pk:proxys) {
			if (pk.getRecipientId().equals(my_uid)) {
				my_proxys.put(pk.getOwnerId(), pk);
			}
		}
		for (EncryptedDocument e:x) {
			if (e.getRecipientId().equals(my_uid)) {
				reps.add(e);
				continue;
			}
			if (my_proxys.containsKey(e.getRecipientId())) {
				EncryptedSessionKey esk = e.getEncryptedSessionKey();
				if (esk.getLevel()!=2) continue;
				ProxyKey pk = my_proxys.get(e.getRecipientId());
				reps.add(ProxyKey.reencrypt(pk,e));
			}
		}
		return reps;
	}

	/**
	 * Send a new Proxy key to the server
	 * This should fail if
	 *  - the user is not logged-in
	 *  - the user is not the owner of the proxy
	 */
	@Override
	public Long postProxy(ProxyKey p) {
		if (p.getId()!=null) return null;
		if (! p.getOwnerId().equals(my_uid)) return null;
		return dao.insertOrUpdate(p);
	}

	/**
	 * Returns the list of all reencryption keys I own 
	 */
	@Override
	public List<ProxyKey> myOutgoingProxys() {
		List<ProxyKey> proxys = dao.getAllProxyKeys();
		List<ProxyKey> my_proxys = new ArrayList<>();
		for (ProxyKey pk:proxys) {
			if (pk.getOwnerId().equals(my_uid)) {
				my_proxys.add(pk);
			}
		}
		return my_proxys;
	}

	/**
	 * Returns the list of all reencryption keys
	 * for which I am the recipient. 
	 */
	@Override
	public List<ProxyKey> myIncomingProxys() {
		List<ProxyKey> proxys = dao.getAllProxyKeys();
		List<ProxyKey> my_proxys = new ArrayList<>();
		for (ProxyKey pk:proxys) {
			if (pk.getRecipientId().equals(my_uid)) {
				my_proxys.add(pk);
			}
		}
		return my_proxys;
	}

	/**
	 * Revoke one of my reencryption keys
	 */
	@Override
	public boolean revokeProxy(Long id) {
		ProxyKey pk = dao.getProxy(id);
		if (pk==null) return false;
		if (! pk.getOwnerId().equals(my_uid)) return false;
		return dao.removeProxy(id);
	}

}
