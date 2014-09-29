package uvsq.m2secrets.proxyreencryption.test;


import java.util.Arrays;

import junit.framework.TestCase;
import uvsq.m2secrets.proxyreencryption.entities.EncryptedDocument;
import uvsq.m2secrets.proxyreencryption.entities.EncryptedSessionKey;
import uvsq.m2secrets.proxyreencryption.entities.Parameters;
import uvsq.m2secrets.proxyreencryption.entities.PrivKey;
import uvsq.m2secrets.proxyreencryption.entities.ProxyKey;
import uvsq.m2secrets.proxyreencryption.entities.PubKey;
import uvsq.m2secrets.proxyreencryption.entities.User;


public class MiracleTest extends TestCase {
	private static PrivKey mprivAlice = null;
	private static PrivKey mprivBob = null; 
	private static PubKey mpubAlice = null;
	private static PubKey mpubBob = null;
	private static User muserAlice = null;
	private static User muserBob = null;
	private static byte[] plaintext = null;
	private static EncryptedDocument encLvl1=null;
	private static EncryptedDocument encLvl2=null;
	private static ProxyKey patob=null;
	private static EncryptedDocument recryp=null;

	public MiracleTest() {}

	private static boolean test1done=false;
	private static boolean test2done=false;
	private static boolean test3done=false;
	private static boolean test3bisdone=false;
	private static boolean test4done=false;
	private static boolean test5done=false;
	private static boolean test6done=false;
	private static boolean test7done=false;
	private static boolean test8done=false;
	private static boolean test9done=false;
	//@Test
	public synchronized void test1PrivAlice() {
		if (test1done) return;
		mprivAlice=PrivKey.generate();
		assertNotNull(mprivAlice);
		assertNotNull(mprivAlice.getA1());
		assertNotNull(mprivAlice.getA2());
		assertNotNull(mprivAlice.getHa1());
		assertNotNull(mprivAlice.getHa2());
		assertNotNull(mprivAlice.getZa1());
		//assertNotNull(mprivAlice.getIA1());
		//assertNotNull(mprivAlice.getIA2());
		//assertEquals(mprivAlice.getIA1(),mprivAlice.getA1().invert());
		//assertEquals(mprivAlice.getIA2(),mprivAlice.getA2().invert());
		assertEquals(mprivAlice.getHa1(),Parameters.h().powZn(mprivAlice.getA1()));
		assertEquals(mprivAlice.getHa2(),Parameters.h().powZn(mprivAlice.getA2()));
		assertEquals(mprivAlice.getZa1(),Parameters.z().powZn(mprivAlice.getA1()));
		test1done=true;
	}

	//@Test
	public synchronized  void test2PubAlice() {
		if (test2done) return;
		test1PrivAlice();
		mpubAlice=new PubKey(mprivAlice);
		assertEquals(mprivAlice.getZa1(), mpubAlice.getZa1());
		assertEquals(mprivAlice.getHa2(), mpubAlice.getHa2());
		test2done=true;
	}

	//@Test
	public synchronized  void test3UserAlice() {
		if (test3done) return;
		test2PubAlice();
		muserAlice=new User();
		muserAlice.setId(1l);
		muserAlice.setName("Alice");
		muserAlice.setPubKey(mpubAlice);
		test3done=true;
	}

	//@Test
	public synchronized void test3UserBob() {
		if (test3bisdone) return;
		test3UserAlice();
		mprivBob=PrivKey.generate();
		mpubBob=new PubKey(mprivBob);
		muserBob=new User();
		muserBob.setId(2l);
		muserBob.setName("Bob");
		muserBob.setPubKey(mpubBob);
		test3bisdone=true;
	}


	//@Test
	public synchronized  void test4Encrypt1() {
		if (test4done) return;
		test3UserBob();
		User useralice = muserAlice;
		String message = "Voici un message. Il est un peu long, mais c'est pour tester le bon fonctionnement du chiffrement, du déchiffrement, et du proxy";
		plaintext = message.getBytes();
		encLvl1 = EncryptedDocument.encrypt(plaintext, useralice, 1);
		assertNotNull(encLvl1);
		assertNotNull(encLvl1.getEncryptedSessionKey());
		assertNotNull(encLvl1.getEncryptedBody());
		assertEquals(encLvl1.getRecipientId(),useralice.getId());
		EncryptedSessionKey esk = encLvl1.getEncryptedSessionKey();
		assertEquals(esk.getLevel(),new Integer(1));
		assertNotNull(esk.getZa1k());
		assertNotNull(esk.getMzk());
		test4done=true;
	}

	//@Test
	public  synchronized void test5DecryptLvl1() {
		if (test5done) return;
		test4Encrypt1();
		byte[] dec = EncryptedDocument.decryptDocument(encLvl1, mprivAlice);
		assertTrue(Arrays.equals(dec, plaintext));
		test5done=true;
	}

	//@Test
	public  synchronized void test6Encrypt2() {
		if (test6done) return;
		test5DecryptLvl1();
		User useralice = muserAlice;
		encLvl2 = EncryptedDocument.encrypt(plaintext, useralice, 2);
		assertNotNull(encLvl2);
		assertNotNull(encLvl2.getEncryptedSessionKey());
		assertNotNull(encLvl2.getEncryptedBody());
		assertEquals(encLvl2.getRecipientId(),useralice.getId());
		EncryptedSessionKey esk = encLvl2.getEncryptedSessionKey();
		assertEquals(esk.getLevel(), new Integer(2));
		assertNotNull(esk.getGk());
		assertNotNull(esk.getMza1k());
		test6done=true;
	}

	//@Test
	public  synchronized void test7DecryptLvl2() {
		if (test7done) return;
		test6Encrypt2();
		byte[] dec = EncryptedDocument.decryptDocument(encLvl2, mprivAlice);
		assertTrue(Arrays.equals(dec, plaintext));
		test7done=true;
	}

	//@Test
	public  synchronized void test8Proxy() {
		if (test8done) return;
		test7DecryptLvl2();
		patob = ProxyKey.delegate(muserAlice, muserBob, mprivAlice);
		recryp = ProxyKey.reencrypt(patob,encLvl2);
		assertNotNull(recryp);
		assertNotNull(recryp.getEncryptedSessionKey());
		assertNotNull(recryp.getEncryptedBody());
		assertEquals(recryp.getRecipientId(),muserBob.getId());
		EncryptedSessionKey esk = recryp.getEncryptedSessionKey();
		assertEquals(esk.getLevel(), new Integer(3));
		assertNotNull(esk.getMza1k());
		assertNotNull(esk.getZa1b2k());
		test8done=true;
	}

	//@Test
	public  synchronized  void test9DecryptLvl3() {
		if (test9done) return;
		test8Proxy();
		byte[] dec = EncryptedDocument.decryptDocument(recryp, mprivBob);
		assertTrue(Arrays.equals(plaintext, dec));
		test9done=true;
	}
}
