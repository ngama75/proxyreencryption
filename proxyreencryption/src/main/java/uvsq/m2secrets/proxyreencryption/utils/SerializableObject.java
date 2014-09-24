package uvsq.m2secrets.proxyreencryption.utils;

import java.io.Serializable;
import java.lang.reflect.Field;

@SuppressWarnings("serial")
public class SerializableObject implements Serializable {
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (Field field : this.getClass().getDeclaredFields()) {
		    // You might want to set modifier to public first.
		    field.setAccessible(true);
		    try {
		    Object value = field.get(this);
		    if (value!=null && value instanceof byte[])
		    	sb.append("\""+field.getName() + "\" : \"" + Utils.byteArrayToHexString((byte[])value) +"\",\n");
		    else
		    sb.append("\""+field.getName() + "\" : \"" + value +"\",\n");
		    } catch (Exception e) {}
		}
		sb.append("}");
		return sb.toString();
	}

}
