package de.svws_nrw.base.email;

import jakarta.validation.constraints.NotNull;

/**
 * Die Konfiguration für eine SMTP-Session
 */
public class MailSmtpSessionConfig {

	// Gibt an, ob Start-TLS verwendet werden soll oder nicht
	private boolean _starttls = true;

	// Die IP-Addresse oder der DNS-Name des SMTP-Hosts
	private final @NotNull String _host;

	// Der Port unter welchem er SMTP-Server erreichbar ist
	private int _port = 25;

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
		this._host = host;
		this._username = username;
		this._password = password;
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
