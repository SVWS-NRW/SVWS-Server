package de.svws_nrw.base.email;

import java.util.Locale;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

/**
 * Die Konfiguration für eine SMTP-Session
 */
public class MailSmtpSessionConfig {

	// Gibt an, ob Start-TLS verwendet werden soll oder nicht
	private boolean _starttls = false;

	// Gibt an, on TLS verwendet werden soll oder nicht
	private boolean _tls = false;

	// Der SHA-256-Fingerabdruck des Zertifikats, dem die Session vertraut, oder null für die Prüfung gegen den Truststore von Java
	private String _zertifikatFingerabdruck = null;

	// Die IP-Addresse oder der DNS-Name des SMTP-Hosts
	private final @NotNull String _host;

	// Der Port unter welchem er SMTP-Server erreichbar ist
	private int _port = 25;

	// Die Zeitgrenze in Millisekunden für den Verbindungsaufbau zum SMTP-Server
	private int _connectionTimeout = 30000;

	// Die Zeitgrenze in Millisekunden für jedes einzelne Lesen und Schreiben auf der Verbindung
	private int _timeout = 60000;

	// Der Username für die Authentifizierung beim SMTP-Host
	private final @NotNull String _username;

	// Das Kennwort für die Authentifizierung beim SMTP-Host
	private final @NotNull String _password;


	/**
	 * Erstellt eine neue SMTP-Session-Konfiguration mit dem angegebenen Host, Username und Kennwort.
	 * Es wird als Default davon ausgegangen, das Start-TLS verwendet wird und der SMTP-Server auf Port 25 lauscht.
	 *
	 * @param host       der SMTP-Host
	 * @param username   der Benutzername
	 * @param password   das Kennwort
	 */
	public MailSmtpSessionConfig(final @NotNull String host, final @NotNull String username, final @NotNull String password) {
		// @NotNull sichert nicht gegen die Übergabe von null. SonarQube denkt aber so und meldet bei Prüfung mittels "== null" immer
		// "java:S2589, Remove this expression which always evaluates to true/false.". Daher hier die Prüfung mittels Objects.requireNonNull.
		try {
			Objects.requireNonNull(password);
			if (Objects.requireNonNull(host).isBlank() || Objects.requireNonNull(username).isBlank()) {
				throw new IllegalArgumentException("Notwendige Parameter für die Erzeugung eines E-Mail-SmtpSessionConfig sind leer.");
			}
		} catch (final NullPointerException e) {
			throw new IllegalArgumentException("Notwendige Parameter für die Erzeugung eines E-Mail-SmtpSessionConfig sind null.");
		}
		this._host = host;
		this._username = username;
		this._password = password;
	}

	/**
	 * Gibt zurück, ob TLS verwendet wird.
	 *
	 * @return true, falls TLS verwendet wird
	 */
	public boolean isTLS() {
		return _tls;
	}

	/**
	 * Setzt in der Konfiguration, ob TLS verwendet werden soll.
	 *
	 * @param tls   true, falls TLS verwendet werden soll und ansonsten false
	 */
	public void setTLS(final boolean tls) {
		this._tls = tls;
	}

	/**
	 * Gibt zurück, ob Start-TLS verwendet wird.
	 *
	 * @return true, falls Start-TLS verwendet wird
	 */
	public boolean isStartTLS() {
		return _starttls;
	}

	/**
	 * Setzt in der Konfiguration, ob Start-TLS verwendet werden soll.
	 *
	 * @param starttls   true, falls Start-TLS verwendet werden soll und ansonsten false
	 */
	public void setStartTLS(final boolean starttls) {
		this._starttls = starttls;
	}

	/**
	 * Gibt den SHA-256-Fingerabdruck des Zertifikats zurück, dem die Session vertraut.
	 *
	 * @return der Fingerabdruck in einheitlicher Form oder null, wenn das Zertifikat gegen den Truststore von Java geprüft wird
	 */
	public String getZertifikatFingerabdruck() {
		return _zertifikatFingerabdruck;
	}

	/**
	 * Setzt den SHA-256-Fingerabdruck des einzigen Zertifikats, dem die Session vertraut. So kann eine Schule auch ein
	 * Zertifikat nutzen, dem Java nicht vertraut, zum Beispiel ein selbst erstelltes.
	 *
	 * @param fingerabdruck   der Fingerabdruck oder null bzw. leer für die Prüfung gegen den Truststore von Java
	 *
	 * @throws IllegalArgumentException   falls die Angabe kein SHA-256-Fingerabdruck ist
	 */
	public void setZertifikatFingerabdruck(final String fingerabdruck) {
		this._zertifikatFingerabdruck = normalisiereFingerabdruck(fingerabdruck);
	}

	/**
	 * Bringt einen SHA-256-Fingerabdruck in die einheitliche Form: 32 Bytes hexadezimal in Großbuchstaben, getrennt durch
	 * Doppelpunkte. Doppelpunkte und Leerzeichen in der Eingabe sind erlaubt, weil Browser und openssl den Abdruck so anzeigen.
	 *
	 * @param fingerabdruck   der Fingerabdruck oder null
	 *
	 * @return der Fingerabdruck in einheitlicher Form oder null, falls die Eingabe null oder leer ist
	 *
	 * @throws IllegalArgumentException   falls die Angabe kein SHA-256-Fingerabdruck ist
	 */
	public static String normalisiereFingerabdruck(final String fingerabdruck) {
		if (fingerabdruck == null) {
			return null;
		}
		final String hex = fingerabdruck.replaceAll("[:\\s]", "").toUpperCase(Locale.ROOT);
		if (hex.isEmpty()) {
			return null;
		}
		if (!hex.matches("[0-9A-F]{64}")) {
			throw new IllegalArgumentException("Die Angabe ist kein SHA-256-Fingerabdruck aus 32 Bytes in hexadezimaler Schreibweise.");
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			if (i > 0) {
				sb.append(':');
			}
			sb.append(hex, i, i + 2);
		}
		return sb.toString();
	}

	/**
	 * Gibt den Port zurück, auf dem der SMTP-Server erwartet wird.
	 *
	 * @return die Port-Adresse des SMTP-Servers
	 */
	public int getPort() {
		return _port;
	}

	/**
	 * Setzt die Port-Adresse, auf dem der SMTP-Server erwartet wird
	 *
	 * @param port   die Port-Adresse
	 */
	public void setPort(final int port) {
		this._port = port;
	}

	/**
	 * Gibt die Zeitgrenze für den Verbindungsaufbau zum SMTP-Server zurück.
	 *
	 * @return die Zeitgrenze in Millisekunden
	 */
	public int getConnectionTimeout() {
		return _connectionTimeout;
	}

	/**
	 * Setzt die Zeitgrenze für den Verbindungsaufbau zum SMTP-Server. Nur positive Werte sind erlaubt,
	 * weil die Mail-Bibliothek 0 als unbegrenztes Warten versteht.
	 *
	 * @param connectionTimeout   die Zeitgrenze in Millisekunden
	 *
	 * @throws IllegalArgumentException   falls die Zeitgrenze nicht positiv ist
	 */
	public void setConnectionTimeout(final int connectionTimeout) {
		if (connectionTimeout <= 0) {
			throw new IllegalArgumentException("Die Zeitgrenze für den Verbindungsaufbau muss positiv sein.");
		}
		this._connectionTimeout = connectionTimeout;
	}

	/**
	 * Gibt die Zeitgrenze für jedes einzelne Lesen und Schreiben auf der Verbindung zurück.
	 *
	 * @return die Zeitgrenze in Millisekunden
	 */
	public int getTimeout() {
		return _timeout;
	}

	/**
	 * Setzt die Zeitgrenze für jedes einzelne Lesen und Schreiben auf der Verbindung. Nur positive Werte sind erlaubt,
	 * weil die Mail-Bibliothek 0 als unbegrenztes Warten versteht.
	 *
	 * @param timeout   die Zeitgrenze in Millisekunden
	 *
	 * @throws IllegalArgumentException   falls die Zeitgrenze nicht positiv ist
	 */
	public void setTimeout(final int timeout) {
		if (timeout <= 0) {
			throw new IllegalArgumentException("Die Zeitgrenze für das Lesen und Schreiben muss positiv sein.");
		}
		this._timeout = timeout;
	}

	/**
	 * Gibt den Hostnamen des SMTP-Server zurück.
	 *
	 * @return der Hostname
	 */
	public String getHost() {
		return _host;
	}

	/**
	 * Gibt den Benutzernamen dieser Session-Konfiguration zurück.
	 *
	 * @return der Benutzername
	 */
	public String getUsername() {
		return _username;
	}

	/**
	 * Gibt das Kennwort dieser Session-Konfiguration zurück.
	 *
	 * @return das Kennwort
	 */
	public String getPassword() {
		return _password;
	}

}
