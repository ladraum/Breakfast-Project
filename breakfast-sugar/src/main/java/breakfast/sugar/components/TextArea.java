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
package breakfast.sugar.components;

import java.io.IOException;
import java.io.PrintWriter;

import breakfast.coffee.util.StringUtil;
import breakfast.coffee.util.Util;
import breakfast.sugar.Widget;


public class TextArea extends Widget {
	
	private String cols;
	private String rows;
	private boolean readonly = false;

	@Override
	public void configure() {
		if (Util.isNull(getWidth()))
			setWidth("260px");
		if (Util.isNull(getHeight()))
			setHeight("80px");
	}

	@Override
	public void render() throws IOException {
		PrintWriter writer = getWriter();
		writer
			.append("<textarea name=\"")
				.append(getId())
			.append("\" id=\"")
				.append(getId())
			.append("\" style=\"")
				.append(getStyleDefinition())
			.append("\" class=\"")
				.append(getSkin())
			.append("\" ");

		if (!StringUtil.isEmpty(getRows()))
			writer.append(" rows=\"").append(getRows()).append('"');
		if (!StringUtil.isEmpty(getCols()))
			writer.append(" cols=\"").append(getCols()).append('"');
		if (isReadonly())
			writer.append(" readonly=\"readonly\" ");

		writer
			.append(getEventsDefinition())
			.append(" >")
			.append(getAttributeValue("value"))
			.append("</textarea>")
			.append("<script>application.addChild(new TextArea( {required:")
			.append(isRequired().toString())
			.append(",label:\"")
				.append(getLabel())
			.append("\",")
			.append("id:\"")
				.append(getId())
			.append("\", readonly:")
				.append(isReadonly().toString())
			.append("}));</script>");
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getCols() {
		return cols;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getRows() {
		return rows;
	}
	
	public String getReadonly() {
		return isReadonly() ? "readonly" : "";
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public Boolean isReadonly() {
		return readonly;
	}

}
