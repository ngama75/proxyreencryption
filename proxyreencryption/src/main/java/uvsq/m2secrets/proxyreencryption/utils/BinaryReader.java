package uvsq.m2secrets.proxyreencryption.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * This class reads a serialized stream of byte arrays, 
 * a
 */
public class BinaryReader {
	private ByteArrayInputStream bi;

	public BinaryReader(byte[] data) {
		bi = new ByteArrayInputStream(data);
	}
	public byte[] next() {
		try {
			//read the length
			byte[] bl = new byte[8];
			if (bi.read(bl)!=8) {
				throw new RuntimeException("End of data in BinaryReader?");
			}
			long s = 0;
			for (int i=7; i>=0; i--) {
				s <<= 8;
				s += (long) bl[i] & 255l;
			}
			if (s==-1) return null;
			if (s<0 || s>100000000000l) {
				throw new RuntimeException("Parse error in BinaryReader?");
			}
			//read the data
			byte[] reps = new byte[(int) s];
			if (bi.read(reps)!=s) 
				throw new RuntimeException("Format error in BinaryReader");
			return reps;
		} catch (IOException e) {}
		return null;
	}
}
