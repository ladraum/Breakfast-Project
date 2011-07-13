package coffee.core.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import coffee.core.util.Util;

public class ClassPathResourceLoader extends AbstractSystemResourceLoader {

	private static final String DEFAULT_RESOURCE_PATH = "";

	public ClassPathResourceLoader(ClassLoader classLoader) {
		setClassLoader(classLoader);
	}

	public List<String> retrieveAvailableResources() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		ClassLoader classLoader = getClassLoader();

		Enumeration<URL> resources = classLoader.getResources(DEFAULT_RESOURCE_PATH);
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String fileName = resource.getFile();
			String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
			File file = new File(fileNameDecoded);

			if (!file.exists())
				continue;

			list.addAll(
				retrieveAvailableResources(file, DEFAULT_RESOURCE_PATH));
		}

		return list;
	}

	public List<String> retrieveAvailableResources(File directory, String path) {
		ArrayList<String> list = new ArrayList<String>();

		File[] listOfFiles = directory.listFiles();
		if (Util.isNull(listOfFiles))
			return list;

		for (File file : listOfFiles) {
        	String fileName = path + "/" + file.getName();
            if (file.isDirectory())
            	list.addAll(
        			retrieveAvailableResources(file, fileName));
            else
            	list.add(fileName);
        }

		return list;
	}
}
