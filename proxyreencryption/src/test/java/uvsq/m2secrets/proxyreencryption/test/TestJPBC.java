package uvsq.m2secrets.proxyreencryption.test;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

public class TestJPBC {
	public static void main(String[] args) throws Exception {
        //ce programme donne un exemple de paramètres.
		//l'un d'entre eux a été entré en dur dans la classe Parameters
		int rBits = 160;
		int qBits = 512;
		TypeACurveGenerator pg = new TypeACurveGenerator(rBits, qBits);
		PairingParameters pp = pg.generate();
		System.out.println(pp.toString());		

	}
}
