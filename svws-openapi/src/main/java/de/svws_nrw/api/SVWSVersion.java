package de.svws_nrw.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import de.svws_nrw.base.ResourceUtils;

/**
 * Diese Klasse stellt Hilfs-Methoden rund um die SVWS-Version zur Verfügung.
 */
public final class SVWSVersion {

	/** Die Version des SVWS-Servers aus der version.properties (cache-Wert) */
	private static String _version = null;

	/** Die erste kompatible Schild-Version aus der version.properties (cache-Wert) */
	private static String _schildMinVersion = null;


	private SVWSVersion() {
		// do nothing
	}


	/**
	 * Ermittelt die Version des SVWS-Servers anhand der version.properties.
	 *
	 * @return die Version des SVWS-Servers
	 */
	public static String version() {
		if (_version == null) {
			final Properties versionProperties = new Properties();
			final Path path = ResourceUtils.getFile("config/version.properties");
			try (InputStream is = Files.newInputStream(path)) {
				versionProperties.load(is);
				_version = versionProperties.getProperty("svws.version");
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return _version;
	}

	/**
	 * Ermittelt die erste kompatible Schild-Version aus der version.properties.
	 *
	 * @return die erste kompatible Schild-Version
	 */
	public static String getSchildMinVersion() {
		if (_schildMinVersion == null) {
			final Properties versionProperties = new Properties();
			final Path path = ResourceUtils.getFile("config/version.properties");
			try (InputStream is = Files.newInputStream(path)) {
				versionProperties.load(is);
				_schildMinVersion = versionProperties.getProperty("schild.version");
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return _schildMinVersion;
	}

}
