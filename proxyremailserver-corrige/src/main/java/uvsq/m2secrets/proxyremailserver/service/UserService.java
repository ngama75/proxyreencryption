package uvsq.m2secrets.proxyremailserver.service;

import javax.ejb.Remote;

import uvsq.m2secrets.proxyreencryption.entities.User;


@Remote
public interface UserService {
    public Long insertOrUpdate(User user);
}
