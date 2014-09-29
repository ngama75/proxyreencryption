package uvsq.m2secrets.proxyremailclient;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import uvsq.m2secrets.proxyreencryption.entities.PrivKey;
import uvsq.m2secrets.proxyreencryption.entities.PubKey;
import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.service.UserService;

public class RegisterUser {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Entrez le nom d'utilisateur?");
		String username = br.readLine();
		System.out.println("Is it the default user (yes or no)?");
		String reps = br.readLine();
		boolean isDefault = true;
		if (reps.equalsIgnoreCase("no")) isDefault=false;
		
		PrivKey pri = PrivKey.generate();
		User u = new User();
		u.setName(username);
		u.setPubKey(new PubKey(pri));
		
		UserService usv = ProxyGfClientUtils.getUserService();
		Long uid = usv.insertOrUpdate(u);
		u.setId(uid);
		
		System.out.println("User Created:"+u);
		
		ProxyGfClientUtils.saveLocalCredentials(u, pri);
		if (isDefault)
			ProxyGfClientUtils.saveLocalDefaultCredentials(u, pri);
	}

}