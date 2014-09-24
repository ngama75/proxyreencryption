package uvsq.m2secrets.proxyremailserver.dao;

import java.util.List;

import javax.ejb.Local;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyreencryption.entities.User;

@Local
public interface MainDao {
    public Long insertOrUpdate(User user);
    public List< User> getAllUsers();
    public Long insertOrUpdate(EncryptedDocument edoc);
    public List<EncryptedDocument> getAllEncryptedDocuments();
    public Long insertOrUpdate(ProxyKey edoc);
    public List<ProxyKey> getAllProxyKeys();
    //-
	public ProxyKey getProxy(Long id);
	public boolean removeProxy(Long id);
	public User getUser(String name);
	public User getUser(Long uid);
}
