package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;
import uvsq.m2secrets.proxyreencryption.utils.BinaryReader;
import uvsq.m2secrets.proxyreencryption.utils.BinaryWriter;
import uvsq.m2secrets.proxyreencryption.utils.Utils;

public class EncryptedSessionKey {
	private Integer level;
	private Element e1;
	private Element e2;
	
	public EncryptedSessionKey() {}
	public EncryptedSessionKey(byte[] data) { deserializeFrom(data); }
	
	public void setLevel1(Element za1k, Element mzk) {
		this.level=1;
		this.e1=za1k;
		this.e2=mzk;		
	}
	public void setLevel2(Element gk, Element mza1k) {
		this.level=2;
		this.e1=gk;
		this.e2=mza1k;		
	}
	public void setLevel3(Element za1b2k, Element mza1k) {
		this.level=3;
		this.e1=za1b2k;
		this.e2=mza1k;		
	}
	
	public Integer getLevel() {
		return level;
	}
	public Element getMzk() {
		return e2;
	}
	public Element getZa1k() {
		return e1;
	}
	public Element getGk() {
		return e1;
	}
	public Element getMza1k() {
		return e2;
	}
	public Element getZa1b2k() {
		return e1;
	}
	
	public byte[] toBytes() {
		BinaryWriter bw = new BinaryWriter();
		bw.writeBytes(Utils.serializeThat(level));
		bw.writeBytes(e1.toBytes());
		bw.writeBytes(e2.toBytes());
		return bw.toByteArray();
	}
	
	public void deserializeFrom(byte[] data) {
		BinaryReader br = new BinaryReader(data);
		level =  (Integer) Utils.deserializeThat(br.next(), Integer.class);
		if (level==1) {
			e1 = Parameters.GT().newElementFromBytes(br.next());
			e2 = Parameters.GT().newElementFromBytes(br.next());
		} else if (level==2) {
			e1 = Parameters.G1().newElementFromBytes(br.next());
			e2 = Parameters.GT().newElementFromBytes(br.next());
		} else if (level==3) {
			e1 = Parameters.GT().newElementFromBytes(br.next());
			e2 = Parameters.GT().newElementFromBytes(br.next());
		} else throw new RuntimeException("wrong level "+level);			
	}
}
