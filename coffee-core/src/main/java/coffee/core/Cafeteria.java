/**
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package coffee.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coffee.core.loader.CoffeeResourceLoader;
import coffee.core.util.Util;

public class Cafeteria {
	
	private static final Map<String, CoffeeResourceLoader> resourceLoaders = new HashMap<String, CoffeeResourceLoader>();

	public static CoffeeContext createContext() {
		return new CoffeeContext();
	}
	
	public static CoffeeResourceLoader getResourceLoader(CoffeeContext context) throws IOException, ClassNotFoundException {
		return getResourceLoader(context.getContextPath());
	}
	
	public static CoffeeResourceLoader getResourceLoader(String contextPath) throws IOException, ClassNotFoundException {
		CoffeeResourceLoader loader = resourceLoaders.get(contextPath);
		if (Util.isNull(loader)) {
			loader = new CoffeeResourceLoader();
			resourceLoaders.put(contextPath, loader);
		}
		return loader;
	}

}
