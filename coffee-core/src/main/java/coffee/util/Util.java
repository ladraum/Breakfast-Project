package coffee.util;


public class Util {

	public static boolean isNull(java.lang.Object object) {
		return object == null;
	}
	
	public static String join(String[] strings, String separator) {
		StringBuffer buffer = new StringBuffer();
		for (String string : strings)
			buffer.append(String.format("%s%s", string, separator));
		buffer = buffer.replace(buffer.length()-1, buffer.length(), "");
		return buffer.toString();
	}

}
