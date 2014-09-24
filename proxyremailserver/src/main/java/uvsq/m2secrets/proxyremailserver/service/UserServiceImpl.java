package uvsq.m2secrets.proxyremailserver.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.dao.MainDao;

@Stateless
public class UserServiceImpl implements UserService {

	@EJB
	private MainDao dao;
	
	@Override
	public Long insertOrUpdate(User user) {
		return dao.insertOrUpdate(user);
	}

}
