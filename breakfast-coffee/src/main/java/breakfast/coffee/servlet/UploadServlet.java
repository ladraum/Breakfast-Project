package breakfast.coffee.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = -3294133664104342850L;
	private Map<String, InputStream> files;
	private Map<String, String> params;
	
	public UploadServlet() {
		files = new HashMap<String, InputStream>();
		params = new HashMap<String, String>();
	}

	public void parseRequest(HttpServletRequest request) throws ServletException, IOException, FileUploadException {
	    for (Part part : request.getParts()) {
	        String filename = getFilename(part);
	        if (filename == null) {
	            String fieldname = part.getName();
	            String fieldvalue = getValue(part);
	            params.put(fieldname, fieldvalue);
	        } else if (!filename.isEmpty()) {
	        	if (reachedMaxFileSize(part))
	        		throw new FileUploadException(FileUploadException.MAX_FILE_SIZE_REACHED);
	            filename = filename
	            				.substring(filename.lastIndexOf('/') + 1)
	            				.substring(filename.lastIndexOf('\\') + 1);
	            InputStream filecontent = part.getInputStream();
	            files.put(filename, filecontent);
	        }
	    }
	}

	public boolean reachedMaxFileSize(Part part) {
		return false;
	}

	public String getFilename(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}

	public String getValue(Part part) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
	    StringBuilder value = new StringBuilder();
	    char[] buffer = new char[1024];
	    for (int length = 0; (length = reader.read(buffer)) > 0;) {
	        value.append(buffer, 0, length);
	    }
	    return value.toString();
	}

	public void setFiles(Map<String, InputStream> files) {
		this.files = files;
	}

	public Map<String, InputStream> getFiles() {
		return files;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return params;
	}
}
