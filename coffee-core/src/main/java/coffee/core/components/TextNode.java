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
package coffee.core.components;

import java.io.IOException;
import java.io.PrintWriter;

import coffee.core.util.StringUtil;

public class TextNode extends AbstractComponent {

	public TextNode() {}

	public TextNode(String content) {
		this.setTextContent(content);
	}

	@Override
	public void configure() {}

	@Override
	public void render() throws IOException {
		PrintWriter writer = coffeeContext.getResponse().getWriter();
		String value = getTextContent();
		if (!StringUtil.isEmpty(value))
			writer.append(value);
	}

}
