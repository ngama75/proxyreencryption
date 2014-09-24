package uvsq.m2secrets.proxyreencryption.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BinaryWriter {
	private ByteArrayOutputStream out;
	public BinaryWriter() {
		out = new ByteArrayOutputStream();
	}
	public void writeBytes(byte[] data) {
		try {
		long length = -1;
		if (data!=null) length = data.length;
		byte[] l = new byte[8];
		for (int i=0; i<8; i++) {
			l[i]=(byte)(length & 255);
			length >>= 8;
		}
		//System.err.print(Utils.toString(l, false));
		out.write(l);
		//System.err.println(" "+Utils.toString(data, false));
		if (data!=null) out.write(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public byte[] toByteArray() {
		try {
			return out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
