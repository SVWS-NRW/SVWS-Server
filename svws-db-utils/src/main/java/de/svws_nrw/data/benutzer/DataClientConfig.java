package de.svws_nrw.data.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.client.DTOClientKonfigurationBenutzer;
import de.svws_nrw.db.dto.current.client.DTOClientKonfigurationGlobal;

/**
 * Diese Klasse stellt Hilfsmethoden für den serverseitigen Zugriff auf die Client-Konfiguration
 * (Tabellen Client_Konfiguration_Benutzer und Client_Konfiguration_Global) bereit.
 */
public final class DataClientConfig {

	private DataClientConfig() {
		// Utility-Klasse
	}

	/**
	 * Liest den Wert des Konfigurationseintrags der angegebenen Client-Anwendung für den angegebenen Schlüssel.
	 * Zunächst wird der benutzerspezifische Eintrag des an der Verbindung angemeldeten Benutzers gelesen. Fehlt dieser,
	 * so wird der globale Eintrag als Fallback verwendet. Die benutzerspezifische Konfiguration kann somit globale
	 * Einstellungen 'überschreiben'.
	 *
	 * @param conn   die Datenbankverbindung mit dem angemeldeten Benutzer
	 * @param app    der Name der Client-Anwendung
	 * @param key    der Schlüssel des Konfigurationseintrags
	 *
	 * @return der Wert des Konfigurationseintrags oder null, wenn weder ein benutzerspezifischer noch ein globaler Eintrag existiert
	 */
	public static String getUserKeyOrGlobal(final DBEntityManager conn, final String app, final String key) {
		final DTOClientKonfigurationBenutzer config = conn.queryByKey(DTOClientKonfigurationBenutzer.class, conn.getUser().getId(), app, key);
		if (config != null) {
			return config.Wert;
		}
		final DTOClientKonfigurationGlobal configGlobal = conn.queryByKey(DTOClientKonfigurationGlobal.class, app, key);
		return (configGlobal == null) ? null : configGlobal.Wert;
	}
}
