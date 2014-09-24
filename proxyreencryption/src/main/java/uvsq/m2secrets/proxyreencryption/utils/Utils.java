package uvsq.m2secrets.proxyreencryption.utils;

import java.lang.reflect.Type;

public class Utils {

	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	public static char hexChar(int x) {
		switch(x) {
		case 0: return '0'; case 1: return '1'; case 2: return '2';
		case 3: return '3'; case 4: return '4'; case 5: return '5';
		case 6: return '6'; case 7: return '7'; case 8: return '8';
		case 9: return '9'; case 10: return 'a'; case 11: return 'b';
		case 12: return 'c'; case 13: return 'd'; case 14: return 'e';
		case 15: return 'f'; 
		}
		return 'X';
	}
	
	public static String byteArrayToHexString(byte[] s) {
	    int len = s.length;
	    StringBuffer reps = new StringBuffer();
	    for (int i = 0; i < len; i++) {
	        reps.append(hexChar((s[i] >> 4)&15));
	        reps.append(hexChar(s[i] & 15));
	    }
	    return reps.toString();
	}

	private static byte[] serializeNumeric(long x,int numbytes) {
		byte[] reps = new byte[numbytes];
		for (int i=0; i<numbytes; i++) {
			reps[i]= (byte) (x % 256);
			x >>= 8;
		}
		return reps;
	}
	private static long deSerializeNumeric(byte[] data) {
		long reps = 0;
		for (int i=data.length-1; i>=0; i--) {
			reps <<= 8;
			reps |= ((long) data[i]) & 255l;
		}
		return reps;
	}
	
	
	public static byte[] serializeThat(Object o) {
		if (o==null) return null;
		if (o instanceof byte[]) return (byte[]) o;
		if (o instanceof String) 
			return ((String) o).getBytes();
		if (o instanceof Long) {
			Long oo = (Long) o;
			return serializeNumeric(oo.longValue(), Long.BYTES);
		}
		if (o instanceof Integer) {
			Integer oo = (Integer) o;
			return serializeNumeric(oo.longValue() , Integer.BYTES);
		}
		if (o instanceof Short) {
			Short oo = (Short) o;
			return serializeNumeric(oo.longValue(), Short.BYTES);
		}
		if (o instanceof Byte) {
			Byte oo = (Byte) o;
			return serializeNumeric(oo.longValue(), Byte.BYTES);			
		}
		throw new RuntimeException("Impossible to serialize "+o.getClass().getName());
	}

	public static Object deserializeThat(byte[] data, Type t) {
		if (data==null) return null;
		if (t.equals(byte[].class))
			return data;
		if (t.equals(String.class))
			return new String(data);
		if (t.equals(Long.class))
			return new Long(deSerializeNumeric(data));
		if (t.equals(Integer.class))
			return new Integer((int) deSerializeNumeric(data));
		if (t.equals(Short.class))
			return new Short((short) deSerializeNumeric(data));
		if (t.equals(Byte.class))
			return new Byte((byte) deSerializeNumeric(data));
		throw new RuntimeException("Impossible to dserialize "+((Class<?>)t));
	}
	
}
