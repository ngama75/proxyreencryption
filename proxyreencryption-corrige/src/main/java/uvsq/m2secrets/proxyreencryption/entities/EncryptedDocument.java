package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@SuppressWarnings("serial")
public class EncryptedDocument implements Serializable {
	private Long id;
	private Long recipientId;
	private byte[] encryptedSessionKey;
	private byte[] encryptedBody;

	public static EncryptedDocument encrypt(byte[] message, User dest, long level) {
	   try {
	   EncryptedDocument doc = new EncryptedDocument();
	   EncryptedSessionKey esk = new EncryptedSessionKey();
	   PubKey pub = dest.getPubKey();
	   Long recipientId = dest.getId();
	   Element m = Parameters.GT().newRandomElement();
	   byte[] aesKey = Parameters.hash_to_byteArray(m, 128);
	   if (level==1) {
		   Element k = Parameters.Zr().newRandomElement();
		   Element zk = Parameters.z().powZn(k);
		   Element za1k = pub.getZa1().duplicate().powZn(k);
		   Element mzk = zk.duplicate().mul(m);
		   esk.setLevel1(za1k,mzk);
	   } else {
		   Element k = Parameters.Zr().newRandomElement();
		   Element gk = Parameters.g().powZn(k);
		   Element za1k = pub.getZa1().duplicate().powZn(k);
		   Element mza1k = za1k.duplicate().mul(m);
		   esk.setLevel2(gk,mza1k);
	   }
	   byte[] enc_body;
	   Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	   SecretKeySpec keyspec = new SecretKeySpec(aesKey, "AES");
	   IvParameterSpec ivspec = new IvParameterSpec(aesKey, 0, cipher.getBlockSize());
	   cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
	   enc_body = cipher.doFinal(message);
	   doc.setRecipientId(recipientId);
	   doc.setEncryptedBody(enc_body);
	   doc.setEncryptedSessionKey(esk);
	   return doc;
	   } catch (Exception e) {
		   throw new RuntimeException(e);
	   }
	}
	public static byte[] decryptDocument(EncryptedDocument edoc,PrivKey priv) {
		try {
		byte[] aesKey=null;
		EncryptedSessionKey esk = edoc.getEncryptedSessionKey();
		if (esk.getLevel()==1) {
			Element inega1 = priv.getA1().duplicate().invert().negate();
			Element izk = esk.getZa1k().duplicate().powZn(inega1);
			Element m = esk.getMzk().duplicate().mul(izk);
			aesKey = Parameters.hash_to_byteArray(m, 128);
		} else if (esk.getLevel()==2) {
			Element za1k = Parameters.pairing().pairing(priv.getHa1(),esk.getGk());
			Element m = esk.getMza1k().div(za1k);
			aesKey = Parameters.hash_to_byteArray(m, 128);
		} else if (esk.getLevel()==3) {
			Element inegb2 = priv.getA2().invert().negate();
			Element iza1k = esk.getZa1b2k().duplicate().powZn(inegb2);
			Element m = esk.getMza1k().duplicate().mul(iza1k);
			aesKey = Parameters.hash_to_byteArray(m, 128);
		}
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keyspec = new SecretKeySpec(aesKey, "AES");
		IvParameterSpec ivspec = new IvParameterSpec(aesKey, 0, cipher.getBlockSize());
		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
		return cipher.doFinal(edoc.getEncryptedBody());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	//autogenerated data
	public EncryptedDocument() {}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}
	public EncryptedSessionKey getEncryptedSessionKey() {
		return new EncryptedSessionKey(encryptedSessionKey);
	}
	public void setEncryptedSessionKey(EncryptedSessionKey encryptedSessionKey) {
		this.encryptedSessionKey = encryptedSessionKey.toBytes();
	}
	public byte[] getEncryptedBody() {
		return encryptedBody;
	}
	public void setEncryptedBody(byte[] encryptedBody) {
		this.encryptedBody = encryptedBody;
	}
	public byte[] getEncryptedSessionKeyRaw() {
		return encryptedSessionKey;
	}
	public void setEncryptedSessionKeyRaw(byte[] encryptedSessionKey) {
		this.encryptedSessionKey=encryptedSessionKey;		
	}
	
}