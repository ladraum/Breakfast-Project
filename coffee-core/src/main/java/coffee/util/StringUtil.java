package coffee.util;

public class StringUtil {

	private StringBuffer buffer;
	
	public StringUtil() {
		buffer = new StringBuffer();
	}
	
	public StringUtil str (String str) {
		buffer.append(str);
		return this;
	}

	public StringUtil str(int i) {
		buffer.append(i);
		return this;
	}

	public StringUtil str(long i) {
		buffer.append(i);
		return this;
	}
	
	public String toString() {
		return buffer.toString();
	}
	
	public static boolean isEmpty(String value) {
		return value == null || value.isEmpty();
	}
}
