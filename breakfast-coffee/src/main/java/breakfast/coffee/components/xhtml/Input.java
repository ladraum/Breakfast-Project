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
package breakfast.coffee.components.xhtml;

import java.io.IOException;

import breakfast.coffee.util.StringUtil;

public class Input extends XHtmlComponent {

	@Override
	public void configure() {
		setComponentName("input");
		setSelfCloseable(true);

		String name = getAttributeAsString("name");
		if (StringUtil.isEmpty(name))
			setAttribute("name", getId());

		String type = getAttributeAsString("type");
		if (type.equals("checkbox"))
			setAttribute("value","true");
		if (type.equals("radio") || type.equals("checkbox"))
			holdExpression(getAttribute("checked").toString());
		else
			holdExpression(getAttribute("value").toString());

	}
	
	@Override
	public void render() throws IOException {
		if (getAttributeAsString("type").equals("checkbox")
		&& !getAttributeAsString("checked").equals("true"))
			ignoreAttribute("checked");

		if (getAttributeAsString("type").equals("radio")
		&& !getAttributeAsString("checked").equals(getAttributeAsString("value")))
			ignoreAttribute("checked");

		super.render();
	}

}
