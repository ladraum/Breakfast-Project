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
package breakfast.coffee.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Defines a Web Resource and its basic configuration.
 */
public @interface WebResource {
	/**
	 * <b>pattern</b> (required): a regular expression pattern to match against
	 * 				  request URI. If the requested URI matches with this pattern then
	 * 				  your web resource will be processed. Remember that the URI patter
	 * 			  	  informed should be relative to the current coffeeContext path.
	 */
	String pattern();
	/**
	 * <b>template</b> (required): the XHTML template file that will be rendered by
	 * 				   web resource.
	 */
	String template();
}
