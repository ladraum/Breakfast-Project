package coffee.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import coffee.core.CoffeeResourceLoader;
import coffee.core.CoffeeResource;
import coffee.sample.HelloWorld;


public class CoffeeResourceLoaderTest {
	
	public static final String PACKAGE_NAME = "coffee.sample";
	private CoffeeResourceLoader resourceManager;

	public CoffeeResourceLoaderTest() {
		resourceManager = CoffeeResourceLoader.getInstance();
	}
	
	@Before
	public void setup(){
		resourceManager.unloadRegisteredResources();
	}

	@Test
	public void retrieveHelloWorldHookResource() throws ClassNotFoundException, IOException {
		resourceManager.loadRegisteredResources(PACKAGE_NAME);
		assertEquals(1, resourceManager.getRegisteredResources().size());
		CoffeeResource resourceData = resourceManager.get("/hello");
		assertNotNull(resourceData);
		assertEquals("templates/hello.xhtml", resourceData.getTemplate());
		assertTrue(resourceData.getClazz().isAssignableFrom(HelloWorld.class));
	}

	@Test
	public void retrieveHelloWorldTemplateResource() throws ClassNotFoundException, IOException {
		resourceManager.loadRegisteredResources(PACKAGE_NAME);
		CoffeeResource resourceData = resourceManager.get("/hello");
		assertNotNull(resourceData);
		String templateName = resourceData.getTemplate();
		InputStream template = resourceManager.loadTemplate(templateName);
		assertNotNull("Template not found: " + templateName,template);
	}

	@Test
	public void assertDoesntRaiseNullPointerExceptionWhileLoadingClasses() throws ClassNotFoundException, IOException {
		try {
			resourceManager.loadRegisteredResources(PACKAGE_NAME);
		} catch (NullPointerException e) {
			fail("It shouldn't raise NullPointerException.");
		}
	}
}
