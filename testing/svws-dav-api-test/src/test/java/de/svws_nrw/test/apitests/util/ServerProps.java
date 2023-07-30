package de.svws_nrw.test.apitests.util;

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
public final class ServerProps {
	private static final String PORT_PROPERTY_KEY = "svws.testing.api.port";
	private static final String HOST_PROPERTY_KEY = "svws.testing.api.host";
	private String host;
	private int port;

	/**
	 * Utility zum Erstellen der Serverproperties aus den Systemproperties. <br>
	 * Dazu sind folgende Systemproperties anzugeben:<br>
	 * <code>svws.testing.api.host</code><br>
	 * <code>svws.testing.api.port</code><br>
	 *
	 * @return die Serverprops, welche in den Systemproperties angegeben sind.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static ServerProps createFromSystemProperties() throws FileNotFoundException, IOException {
		System.out.println(System.getProperties().entrySet());
		if (System.getProperties().keySet().contains(HOST_PROPERTY_KEY)) System.out.println(System.getProperty(HOST_PROPERTY_KEY));
		if (System.getProperties().keySet().contains(PORT_PROPERTY_KEY)) System.out.println(System.getProperty(PORT_PROPERTY_KEY));
		if (System.getProperties().keySet().contains(HOST_PROPERTY_KEY)
				&& System.getProperties().keySet().contains(PORT_PROPERTY_KEY)) {
			System.out.println("Tests mit Systemproperties");
			final ServerProps p = new ServerProps();
			p.host = System.getProperty(HOST_PROPERTY_KEY);
			p.port = Integer.parseInt(System.getProperty(PORT_PROPERTY_KEY));
			return p;
		}
		final File localPropertyFile = new File("local.properties");
		if (!localPropertyFile.exists()) {
			throw new FileNotFoundException("local.properties nicht gefunden.");
		}
		final Properties localProperties = new Properties();
		try (FileInputStream inStream = new FileInputStream(localPropertyFile)) {
			localProperties.load(inStream);
			inStream.close();
			if (!localProperties.containsKey(HOST_PROPERTY_KEY)
					|| !localProperties.containsKey(PORT_PROPERTY_KEY)) {
				throw new IOException("Properties sind unvollständig.");
			}
			final ServerProps p = new ServerProps();
			p.host = localProperties.getProperty(HOST_PROPERTY_KEY);
			p.port = Integer.parseInt(localProperties.getProperty(PORT_PROPERTY_KEY));
			return p;
		}
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

	@Override
	public String toString() {
		return "ServerProps [host=" + host + ", port=" + port + "]";
	}

}
