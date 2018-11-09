package com.example.rest;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ext.ContextResolver;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

import com.example.rest.controller.DataInitializer;
import com.example.rest.persistence.FileHandler;
import com.example.rest.utils.GlobalVariables;

/**
 * Main class.
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	// public static final URI BASE_URI =
	// URI.create("http://localhost:8080/persona/");
	public static String projectPath = null;

	// docker - Base URI the Grizzly HTTP server will listen on
	 public static final String BASE_URI = "http://0.0.0.0:8080/persona/";
	// base URI
	 // public static final String BASE_URI = "http://localhost:8080/persona/";

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 *
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.example.rest package
		final ResourceConfig rc = new ResourceConfig().packages("com.example.rest");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	/**
	 * Main method.
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("BOOOOOOOOOOOOOOOOOOOOM");

		getProjectPath();
		GlobalVariables.init();

		final HttpServer server = startServer();

		System.out.println(String.format("Jersey app started at " + "%ssystem\nHit enter to stop it...", BASE_URI));
		System.out.println("LOOOOOOOOOOOL");
		// System.in.read();
		// server.stop();
	}

	// public static void main(String[] args) {
	// getProjectPath();
	//
	// runRServer();
	//
	//// runRFunction(8);
	// try {
	// System.out.println("Persona Server!");
	//
	// final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI,
	// createApp(), false);
	// Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	// @Override
	// public void run() {
	// server.shutdownNow();
	// }
	// }));
	// server.start();
	// System.out.println(String.format("Application started.%nStop the application
	// using CTRL+C"));
	// Thread.currentThread().join();
	//
	// } catch (IOException | InterruptedException ex) {
	// Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	// }
	// }

	private static void runRServer() {
		String path = projectPath + "RserverScript.R";
		System.out.println(path);
		try {
			Runtime.getRuntime().exec("Rscript " + path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ResourceConfig createApp() {
		return new ResourceConfig().packages("com.example.test.services").register(createMoxyJsonResolver());
		// return new ResourceConfig();
	}

	public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
		final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
		Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
		namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
		return moxyJsonConfig.resolver();
	}

	private static void getProjectPath() {
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		String fullPath = System.getProperty("user.dir");
		projectPath = fullPath + "/";
	}
}
