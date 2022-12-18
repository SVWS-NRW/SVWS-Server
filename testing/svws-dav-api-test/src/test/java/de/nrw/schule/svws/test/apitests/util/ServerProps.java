package de.nrw.schule.svws.test.apitests.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Dies repräsentiert die Servereigenschaften, welche zum durchführen der
 * API-Tests nötig sind.
 *
 */
public class ServerProps {
	private static final String SCHEMA_PROPERTY_KEY = "svws.testing.api.schema";
	private static final String PORT_PROPERTY_KEY = "svws.testing.api.port";
	private static final String HOST_PROPERTY_KEY = "svws.testing.api.host";
	private String host;
	private int port;
	private String schema;

	/**
	 * Utility zum Erstellen der Serverproperties aus den Systemproperties. <br>
	 * Dazu sind folgende Systemproperties anzugeben:<br>
	 * <code>svws.testing.api.host</code><br>
	 * <code>svws.testing.api.port</code><br>
	 * <code>svws.testing.api.schema</code><br>
	 * 
	 * @return die Serverprops, welche in den Systemproperties angegeben sind.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ServerProps createFromSystemProperties() throws FileNotFoundException, IOException {
		if (System.getProperties().entrySet().contains(HOST_PROPERTY_KEY) &&
				System.getProperties().entrySet().contains(PORT_PROPERTY_KEY) &&
				System.getProperties().entrySet().contains(SCHEMA_PROPERTY_KEY)) {
			System.out.println("Tests mit Systemproperties");
			ServerProps p = new ServerProps();
			p.host = System.getProperty(HOST_PROPERTY_KEY);
			p.port = Integer.parseInt(System.getProperty(PORT_PROPERTY_KEY));
			p.schema = System.getProperty(SCHEMA_PROPERTY_KEY);
			return p;
		}
		File file = new File("../local.properties");
		System.out.println("Tests mit Properties aus " + file.getAbsolutePath());
		Properties localProperties = new Properties();
		localProperties.load(new FileInputStream(file));
		ServerProps p = new ServerProps();
		p.host = localProperties.getProperty(HOST_PROPERTY_KEY);
		p.port = Integer.parseInt(localProperties.getProperty(PORT_PROPERTY_KEY));
		p.schema = localProperties.getProperty(SCHEMA_PROPERTY_KEY);
		return p;
	}

	/**
	 * private constructor
	 */
	private ServerProps() {

	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

}
