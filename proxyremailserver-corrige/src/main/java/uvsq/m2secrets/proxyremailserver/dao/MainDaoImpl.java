package uvsq.m2secrets.proxyremailserver.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.dbentities.DBEncryptedDocument;
import uvsq.m2secrets.proxyremailserver.dbentities.DBProxyKey;
import uvsq.m2secrets.proxyremailserver.dbentities.DBUser;

@Stateless
public class MainDaoImpl implements MainDao {
	@PersistenceContext 
	private EntityManager em;

	// Stores a new guest:
    public Long insertOrUpdate(User user) {
    	DBUser u = new DBUser(user);
        em.persist(u);
        em.flush();
        System.err.println("Added user "+u.getId());
        return u.getId();
    }
    // Retrieves all the guests:
    public List<User> getAllUsers() {
        ArrayList<User> reps = new ArrayList<User>();
    	TypedQuery<DBUser> query = em.createQuery(
            "SELECT g FROM DBUser g ORDER BY g.id", DBUser.class);
        for (DBUser u: query.getResultList()) {
        	reps.add(u.getUser());
        }
        return reps;
    }
	@Override
	public Long insertOrUpdate(EncryptedDocument edoc) {
        DBEncryptedDocument dedoc = new DBEncryptedDocument(edoc);
        dedoc = em.merge(dedoc);
        em.flush();
        return dedoc.getId();
	}
	@Override
	public List<EncryptedDocument> getAllEncryptedDocuments() {
        ArrayList<EncryptedDocument> reps = new ArrayList<>();
    	TypedQuery<DBEncryptedDocument> query = em.createQuery(
            "SELECT g FROM DBEncryptedDocument g ORDER BY g.id", 
            DBEncryptedDocument.class);
        for (DBEncryptedDocument u: query.getResultList()) {
        	reps.add(u.getEncryptedDocument());
        }
        return reps;
	}
	@Override
	public Long insertOrUpdate(ProxyKey p) {
        DBProxyKey dp = new DBProxyKey(p);
        dp = em.merge(dp);
        em.flush();
        return dp.getId();
	}
	@Override
	public List<ProxyKey> getAllProxyKeys() {
        ArrayList<ProxyKey> reps = new ArrayList<>();
    	TypedQuery<DBProxyKey> query = em.createQuery(
            "SELECT g FROM DBProxyKey g ORDER BY g.id", 
            DBProxyKey.class);
        for (DBProxyKey u: query.getResultList()) {
        	reps.add(u.getProxyKey());
        }
        return reps;
	}
	@Override
	public ProxyKey getProxy(Long id) {
		DBProxyKey p = em.find(DBProxyKey.class, id);
		if (p!=null) return p.getProxyKey();
		return null;
	}
	@Override
	public boolean removeProxy(Long id) {
		DBProxyKey p = em.find(DBProxyKey.class, id);
		if (p!=null) {
			em.remove(p);
			return true;
		}
		return false;
	}
	@Override
	public User getUser(String name) {
        TypedQuery<DBUser> query = em.createQuery(
            "SELECT g FROM DBUser g WHERE g.name = :name ORDER BY g.id DESC", 
            DBUser.class);
    	DBUser u = query.setParameter("name", name).getSingleResult();
    	return u.getUser();
	}
	@Override
	public User getUser(Long uid) {
		DBUser p = em.find(DBUser.class, uid);
		if (p!=null) return p.getUser();
		return null;
	}
}
