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

public class Input extends HtmlComponent {
	
	private String type;
	private String value;

	@Override
	public void configure() {
		setComponentName("input");
		setSelfCloseable(true);
	}

	public void setName(String name) {
		setId(name);
	}
	
	public void setType(String value) {
		this.type = value;
	}

	public void setValue(String value) {
		if (type.equals("text"))
			holdValue(value);
		this.value = value;
	}

	public String getChecked() {
		String newValue = getAttributeValue("checked");
		if (type.equals("radio") && newValue.equals(value))
			return "checked";
		if (type.equals("checkbox") && newValue.equals("on"))
			return "checked";
		return "";
	}

	public void setChecked(String value) {
		if (type.equals("checkbox")
		||  type.equals("radio"))
			holdValue(value);
	}

}
