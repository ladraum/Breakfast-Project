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
package breakfast.coffee;

import java.io.IOException;

/**
 * @since Coffee 1.0
 */
public class CoffeeResource {

	private String pattern;
	private String template;
	private Class<?> clazz;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
/**
 * The resource hook factory. 
 * @param context
 * @return
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 * @throws IOException 
 */
	public IResource instantiateHook(CoffeeContext context) throws InstantiationException, IllegalAccessException, IOException {
		IResource hook = (IResource)clazz.newInstance();
		hook.configure(context);
		return hook;
	}
}
