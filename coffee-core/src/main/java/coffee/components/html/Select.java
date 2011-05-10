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
package coffee.components.html;

import java.io.IOException;

import coffee.components.IComponent;

public class Select extends HtmlComponent {
	
	private String name;

	@Override
	public void configure() {
		super.configure();
		setComponentName("select");
		ignoreAttribute("selected");
	}
	
	@Override
	public void renderChildren() throws IOException {
		for (IComponent child : getChildren()) {
			String selected = getAttributeValue("selected");
			if (child.getAttribute("value").equals(selected))
				child.setAttribute("selected", "selected");
			child.render();
		}
	}

	public void setName(String name) {
		this.name = name;
		setId(name);
	}

	public String getName() {
		return name;
	}

	public void setSelected(String selected) {
		holdValue(selected);
	}
}
