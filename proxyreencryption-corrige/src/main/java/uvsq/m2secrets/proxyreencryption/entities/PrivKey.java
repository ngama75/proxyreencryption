package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;
import uvsq.m2secrets.proxyreencryption.utils.BinaryReader;
import uvsq.m2secrets.proxyreencryption.utils.BinaryWriter;

public class PrivKey {
	private Element a1;
	private Element a2;
	private Element za1;
	private Element ha2;
	private Element ha1;

	public PrivKey() {}
	public PrivKey(byte[] data) {deserializeFrom(data);}

	public static PrivKey generate() {
		PrivKey reps = new PrivKey();
		reps.a1 = Parameters.Zr().newRandomElement();
		reps.a2 = Parameters.Zr().newRandomElement();
		reps.za1 = Parameters.z().powZn(reps.a1);
		reps.ha2 = Parameters.h().powZn(reps.a2);
		reps.ha1 = Parameters.h().powZn(reps.a1);
		return reps;
	}
	public Element getA1() {
		return a1;
	}
	public void setA1(Element a1) {
		this.a1 = a1;
	}
	public Element getA2() {
		return a2;
	}
	public void setA2(Element a2) {
		this.a2 = a2;
	}
	public Element getZa1() {
		return za1;
	}
	public void setZa1(Element za1) {
		this.za1 = za1;
	}
	public Element getHa2() {
		return ha2;
	}
	public void setHa2(Element ha2) {
		this.ha2 = ha2;
	}
	public Element getHa1() {
		return ha1;
	}
	public void setHa1(Element ha1) {
		this.ha1 = ha1;
	}	
	
	public byte[] toBytes() {
		BinaryWriter bw = new BinaryWriter();
		bw.writeBytes(a1.toBytes());
		bw.writeBytes(a2.toBytes());
		bw.writeBytes(za1.toBytes());
		bw.writeBytes(ha2.toBytes());
		bw.writeBytes(ha1.toBytes());
		return bw.toByteArray();
	}
	public void deserializeFrom(byte[] data) {
		BinaryReader br = new BinaryReader(data);
		a1 = Parameters.Zr().newElementFromBytes(br.next());
		a2 = Parameters.Zr().newElementFromBytes(br.next());
		za1 = Parameters.GT().newElementFromBytes(br.next());
		ha2 = Parameters.G2().newElementFromBytes(br.next());
		ha1 = Parameters.G2().newElementFromBytes(br.next());
	}
}
