package de.svws_nrw.module.reporting.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.types.ServerMode;

/**
 * Statische Klasse mit Hilfsmethoden zu den Eigenschaften des Servers.
 */
public final class ReportingServerUtils {

	/** Die Version des SVWS-Servers aus der version.properties. Wird beim ersten Zugriff gelesen und danach zwischengespeichert. */
	private static String version = null;

	private ReportingServerUtils() {
		throw new IllegalStateException("Statische Klasse mit Hilfsmethoden zu den Eigenschaften des Servers. Initialisierung nicht möglich.");
	}

	/**
	 * Liefert den aktuellen Servermodus zurück. Der Servermodus beschreibt, in welchem Betriebsmodus sich der Server befindet.
	 *
	 * @return Der aktuelle Servermodus.
	 */
	public static ServerMode servermode() {
		return SVWSKonfiguration.get().getServerMode();
	}

	/**
	 * Liefert den aktuellen Servermodus als String zurück. Der Servermodus beschreibt, in welchem Betriebsmodus sich der Server befindet.
	 *
	 * @return Der aktuelle Servermodus als String.
	 */
	public static String servermodetext() {
		return servermode().toString();
	}

	/**
	 * Liefert die aktuelle Version des Servers als String zurück. Die Version wird beim ersten Aufruf aus der Datei
	 * "config/version.properties" gelesen und danach zwischengespeichert; sie ändert sich zur Laufzeit nicht.
	 * Ist die Datei nicht lesbar, wird {@code [unbekannt]} geliefert — eine fehlende Versionsangabe darf keinen Report abbrechen.
	 *
	 * <p><b>Warum hier und nicht über {@code SVWSVersion}:</b> Die Klasse {@code de.svws_nrw.api.common.SVWSVersion} leistet
	 * dasselbe, liegt aber im Modul {@code svws-openapi}. Das Reporting-Modul baut nur auf {@code svws-asd}, {@code svws-core},
	 * {@code svws-db}, {@code svws-db-dto}, {@code svws-db-utils} und {@code svws-module-xschule} auf; eine Abhängigkeit auf die
	 * API-Schicht wäre eine Umkehr der Schichtung. Die doppelte Implementierung ist deshalb Absicht und nicht zusammenzuführen,
	 * solange {@code SVWSVersion} nicht in ein tieferes Modul wandert.</p>
	 *
	 * @return Die aktuelle Version des Servers als String.
	 */
	public static String serverversion() {
		if (version == null) {
			final Properties versionProperties = new Properties();
			final Path path = ResourceUtils.getFile("config/version.properties");
			try (InputStream is = Files.newInputStream(path)) {
				versionProperties.load(is);
				version = versionProperties.getProperty("svws.version", "[unbekannt]");
			} catch (final IOException e) {
				version = "[unbekannt]";
			}
		}
		return version;
	}
}
