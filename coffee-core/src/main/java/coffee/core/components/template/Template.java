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
package coffee.core.components.template;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import coffee.core.CoffeeResourceLoader;
import coffee.core.components.AbstractComponent;
import coffee.core.components.IComponent;

public class Template extends AbstractComponent {
	
	private String src;

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		try {
			CoffeeResourceLoader resourceLoader = CoffeeResourceLoader.getInstance();
			IComponent template = resourceLoader.compile(src, getCoffeeContext());
			template.render();
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (CloneNotSupportedException e) {
			throw new IOException(e);
		}
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}
}
