package coffee.stress;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestThread extends Thread {
	
	private RequestData data;

	@Override
	public void run() {
		URL url;

		try {
			url = new URL(data.getLocation());
			
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			uc.connect();

			InputStream stream = uc.getInputStream();
			while (stream.read() > 0);
				uc.disconnect();

			synchronized (data) {
				data.increaseNumberOfRequestsDones();
				data.notifyAll();
			}
			
			
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public void setData(RequestData data) {
		this.data = data;
	}

	public RequestData getData() {
		return data;
	}

}
