package coffee.core.util.json;

import java.text.SimpleDateFormat;

public class DateSerializer implements IFieldSerializer {
	
	private String mask;
	
	public DateSerializer(String mask) {
		setMask(mask);
	}

	@Override
	public String serialize(Object data) {
		SimpleDateFormat out = new SimpleDateFormat(mask);
		return out.format(data);
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getMask() {
		return mask;
	}

}
