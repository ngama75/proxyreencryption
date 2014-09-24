package uvsq.m2secrets.proxyreencryption.entities;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;

@SuppressWarnings("rawtypes")
public class Parameters {
	private static final Pairing ppairing;
	private static final Field ZZr;
	private static final Field GG1;
	private static final Field GG2;
	private static final Field GGT;
	private static final Element gg;
	private static final Element hh;
	private static final Element zz;
 
	
	static {
		PropertiesParameters params = new PropertiesParameters();
        params.put("type", "a");
        params.put("q", "5804434762417429153630755243384075229125429844551691156416793289119072285554614376260583508527874392768466549960200047079483499004205161028014603728199663");
        params.put("r", "730750818665452757176057050065048642452048576511");
        params.put("h", "7943110858251087352806072808485175333151020627206406469451393501935946698832148679886099489944616327569424");
        params.put("exp1", "110");
        params.put("exp2", "159");
        params.put("sign0", "-1");
        params.put("sign1", "1");
		ppairing = PairingFactory.getPairing(params);
		ZZr = ppairing.getZr();
		GG1 = ppairing.getG1();
		GG2 = ppairing.getG2();
		GGT = ppairing.getGT();
		gg = GG1.newElementFromHash("plouf".getBytes(), 0, 5);
		hh = GG2.newElementFromHash("plouf".getBytes(), 0, 5);
		zz = ppairing.pairing(gg, hh);
	}
	public static Pairing pairing() {return ppairing;}
	public static Element g() {return gg.duplicate();}
	public static Element h() {return hh.duplicate();}
	public static Element z() {return zz.duplicate();}
	public static Field G1() { return GG1; }
	public static Field G2() { return GG2; }
	public static Field GT() { return GGT; }
	public static Field Zr() { return ZZr; }
    //TODO: maybe, put a more serious hash function here? 
	public static byte[] hash_to_byteArray(Element e, int size) {
		int bsize = size/8;
		byte[] b = e.toBytes();
		byte[] reps = new byte[bsize];
		if (b.length==0) {
			for (int i=0; i<bsize; i++) reps[i]=0;
			return reps;
		}
		for (int i=0; i<bsize; i++) {
			reps[i]=b[i%b.length];
		}
		return reps;
	}
}
