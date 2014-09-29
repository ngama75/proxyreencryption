package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;
import uvsq.m2secrets.proxyreencryption.utils.BinaryReader;
import uvsq.m2secrets.proxyreencryption.utils.BinaryWriter;

public class PubKey {
	private Element za1;
	private Element ha2;
	
	public PubKey() {}
	public PubKey(byte[] data) { deserializeFrom(data); }

	public PubKey(PrivKey pri) {
		za1 = pri.getZa1();
		ha2 = pri.getHa2();
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
    
	public void deserializeFrom(byte[] data) {
		BinaryReader br = new BinaryReader(data);
		za1 = Parameters.GT().newElementFromBytes(br.next());
		ha2 = Parameters.G2().newElementFromBytes(br.next());
	}
	public byte[] toBytes() {
		BinaryWriter bw = new BinaryWriter();
		bw.writeBytes(za1.toBytes());
		bw.writeBytes(ha2.toBytes());
		return bw.toByteArray();
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PubKey [za1=");
		builder.append(za1);
		builder.append(", ha2=");
		builder.append(ha2);
		builder.append("]");
		return builder.toString();
	}
	
	
}
