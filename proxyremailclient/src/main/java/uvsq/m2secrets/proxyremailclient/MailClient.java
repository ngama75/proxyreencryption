package uvsq.m2secrets.proxyremailclient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyreencryption.entities.PrivKey;
import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyreencryption.entities.User;
import uvsq.m2secrets.proxyremailserver.service.MailService;


public class MailClient {
	private static User me = null;
	private static MailService ms = null;
	private static PrivKey myprivkey = null;

	private static BufferedReader br = null;
	private static String inputLine() {
		try {
			return br.readLine();
		} catch (Exception e) {
			return "";
		}
	}
	private static long inputLong() {
		while(true) { 
			try {
				String reps = inputLine();
				return Long.parseLong(reps);
			} catch (Exception e) {
				System.err.println("Entrez un nombre!");
			}
		}
	}
	private static int inputInt() {
		return (int) inputLong();
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		ms = ProxyGfClientUtils.getMailService();

		User myself = ProxyGfClientUtils.loadLocalDefaultUser();
		myprivkey = ProxyGfClientUtils.loadLocalDefaultPrivateKey();
		if (myself==null || myprivkey==null) {
			System.err.println("No local credentials found!");
			return;
		}


		System.out.println("try to log-in as: "+myself.getId());
		EncryptedDocument challenge = ms.askLogin(myself.getId());
		byte[] answer = EncryptedDocument.decryptDocument(challenge, myprivkey);
		me = ms.validateLogin(answer);

		if (me==null) {
			System.err.println("impossible to log-in");
			return;
		}
		System.out.println("Logged in as:"+me);
		while (mainMenu()) {};
	}

	private static void listUsers() {
		System.out.println("list of users:");
		Map<Long, String> users = ms.getUserNames();
		for (Entry<Long, String> e :users.entrySet()) {
			System.out.println(" "+e.getKey()+" -- "+e.getValue());
		}
	}
	private static void listOutgoingProxys() {
		System.out.println("List of Outgoing Proxys:");
		List<ProxyKey> oproxys = ms.myOutgoingProxys();
		System.out.println(oproxys.size()+" proxys...");
		for (ProxyKey p:oproxys) {
			System.out.println(p);			
		}
	}

	private static void listIncomingProxys() {
		System.out.println("List of Incoming Proxys:");
		List<ProxyKey> iproxys = ms.myIncomingProxys();
		System.out.println(iproxys.size()+" proxys...");
		for (ProxyKey p:iproxys) {
			System.out.println(p);			
		}
	}

	private static void createProxy() {
		System.out.println("Which user do you want to authorize?");
		long uid = inputLong();
		User dest = ms.findUser(uid);
		if (dest==null) {
			System.err.println("User does not exist!");
			return;
		}
		System.out.println("Generating proxy...");
		ProxyKey p = ProxyKey.delegate(me, dest, myprivkey);
		Long pid = ms.postProxy(p);
		System.out.println("Tada!!! "+pid);
	}

	private static void deleteProxy() {
		System.out.println("Which proxy do you want to delete?");
		long pid = inputLong();
		ms.revokeProxy(pid);
	}

	private static void readMessages() {
		System.out.println("List of Documents:");		
		List<EncryptedDocument> edocs = ms.myEncryptedDocuments();
		System.out.println(edocs.size()+" documents...");
		for (EncryptedDocument edoc:edocs) {
			System.out.println("Message: "+edoc.getId());
			byte[] message = EncryptedDocument.decryptDocument(edoc, myprivkey);
			System.out.println(new String(message));
			System.out.println("--------------------");
		}
	}

	private static void sendMessage() {
		System.out.println("Entrez l'id du destinataire?");
		Long iddest = inputLong();
		
		System.out.println("Which ciphertext level? (1 or 2)");
		Integer level = inputInt();
        User dest = ms.findUser(iddest);
        if (dest==null) {
        	System.err.println("L'utilisateur n'existe pas!");
        	return;
        }
		System.out.println("Entrez un message? (terminer par une ligne vide)");
        StringBuilder sb = new StringBuilder();
        sb.append("From: "+me.getName()+" ("+me.getId()+")\n");
        sb.append("To: "+dest.getName()+" ("+dest.getId()+")\n\n");
        for (String s=inputLine(); !s.isEmpty(); s=inputLine())
        	sb.append(s+"\n");
        byte[] message = sb.toString().getBytes();
		
        System.out.println("Encrypting message...");
        EncryptedDocument edoc = EncryptedDocument.encrypt(message, dest, level);
        System.out.println("Sending Message...");
        Long mid = ms.postEncryptedDocument(edoc);        
        System.out.println("Tada!!! (message id="+mid+")");
	}


	private static boolean mainMenu() {
		System.out.println("========== Menu ============");
		System.out.println("What do you want to do?");
		System.out.println("1. List Users");
		System.out.println("2. List Incoming Proxys");
		System.out.println("3. List Outgoing Proxys");
		System.out.println("4. Create a proxy");
		System.out.println("5. Remove a proxy");
		System.out.println("6. Read all Messages");
		System.out.println("7. Send a Messages");
		System.out.println("8. Exit");
		int lreps = inputInt();

		switch(lreps) {
		case 8: return false;
		case 1: listUsers(); return true; 
		case 2: listIncomingProxys(); return true; 
		case 3: listOutgoingProxys(); return true; 
		case 4: createProxy(); return true; 
		case 5: deleteProxy(); return true; 
		case 6: readMessages(); return true; 
		case 7: sendMessage(); return true; 
		default: return true;
		}
	}
}
