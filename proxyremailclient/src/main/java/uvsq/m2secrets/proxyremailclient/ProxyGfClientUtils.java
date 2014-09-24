package uvsq.m2secrets.proxyremailclient;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import uvsq.m2secrets.proxyreencryption.entities.PrivKey;
import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.service.MailService;
import uvsq.m2secrets.proxyremailserver.service.UserService;


public class ProxyGfClientUtils {
	private static final String JNDI_APP="java:global/proxyremailserver";
	private static InitialContext pctx=null;
	private static MailService pmx=null;
	private static UserService pdao=null;


	private static InitialContext getInitialContext() throws NamingException {
		if (pctx!=null) return pctx;
		System.err.println("connecting to the server... please wait...");
		pctx = new InitialContext();
		//InitialContext ctx = new InitialContext();
		System.err.println("phase 1/2 ok: initial context created!");
		return pctx;	
	}

	public static MailService getMailService() {
		try {
			if (pmx!=null) return pmx;
			InitialContext ctx = getInitialContext();
			pmx = (MailService) ctx.lookup(JNDI_APP+"/MailServiceImpl");
			System.err.println("phase 2/2: Mailservice retrieved: "+pmx);
			return pmx;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static UserService getUserService() {
		try {
			if (pdao!=null) return pdao;
			InitialContext ctx = getInitialContext(); 
			pdao = (UserService) ctx.lookup(JNDI_APP+"/UserServiceImpl");
			System.err.println("phase 2/2: Dao retrieved: "+pdao);
			return pdao;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static PrivKey loadLocalPrivatekey(Long uid) {
		File f = new File("keys/privatekey-"+uid+".bin");
		try {
			FileInputStream ifs = new FileInputStream(f);
			ByteArrayOutputStream obs = new ByteArrayOutputStream();
			for (int b=ifs.read(); b>=0; b=ifs.read())
				obs.write(b);
			ifs.close();
			return new PrivKey(obs.toByteArray());
		} catch (FileNotFoundException e) {
			System.err.println(f+" does not exist!");
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static User loadLocalUser(Long uid) {
		File f = new File("keys/user-"+uid+".bin");
		try {
			ObjectInputStream ifs = new ObjectInputStream(new FileInputStream(f));
			User u = (User) ifs.readObject();
			ifs.close();
			return u;
		} catch (FileNotFoundException e) {
			System.err.println(f+" does not exist!");
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void saveLocalCredentials(User u, PrivKey pri, Long uid) {
		try {
			File f = new File("keys/privatekey-"+uid+".bin");
			FileOutputStream of = new FileOutputStream(f);
			of.write(pri.toBytes());
			of.close();
			System.out.println("private key saved in:"+f);
			File fu = new File("keys/user-"+uid+".bin");
			ObjectOutputStream ofu = new ObjectOutputStream(new FileOutputStream(fu));
			ofu.writeObject(u);
			ofu.close();
			System.out.println("user saved in:"+fu);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void saveLocalCredentials(User u, PrivKey pri) {
		saveLocalCredentials(u, pri, u.getId());
	}
	public static void saveLocalDefaultCredentials(User u, PrivKey pri) {
		saveLocalCredentials(u, pri, 0l);
	}
	public static User loadLocalDefaultUser() {
		return loadLocalUser(0l);
	}
	public static PrivKey loadLocalDefaultPrivateKey() {
		return loadLocalPrivatekey(0l);
	}
}
