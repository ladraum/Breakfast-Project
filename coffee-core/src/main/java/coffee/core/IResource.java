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

/**
 * The web resource interface. It defines the basic life cycle of
 * a web resource on the Coffee Framework. Software developers that
 * looks for a robust web resource should use <i>{@link Resource}</i>
 * instead of this interface.
 *  
 * @since Coffee 1.0
 */
public interface IResource {

/**
 * Configure the web resource. It allows to feed the context with data
 * that will be handle by the coffee parser (like value expressions, for
 * an example).
 * @param context
 */
	public abstract void configure(CoffeeContext context);

/**
 * The main method from the web resource. This method was designed to make possible
 * to retrieve data that will be rendered on the browser, or persist browser sent data
 * before redirect the user to a success/fail page. 
 */
	public abstract void process();
	
}
