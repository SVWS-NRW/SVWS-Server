package de.svws_nrw.core.types.oauth2;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.types.ServerMode;
import jakarta.validation.constraints.NotNull;

/**
 * Die Betriebsmodi der externen OAuth-Systeme (Test- bzw. Produktivumgebung).
 * Das Enum wird als Spaltentyp der OAuth-Konfigurationstabellen genutzt, das
 * Enum {@link ServerMode} dagegen nicht.
 */
public enum OAuthServerMode {

	/** Die Testumgebung der externen Systeme. Wird von den Server-Modi BETA, ALPHA und DEV genutzt. */
	DEV("DEVELOPMENT"),

	/** Die Produktivumgebung der externen Systeme. Wird vom Server-Modus STABLE genutzt. */
	PROD("PRODUCTION");


	private final @NotNull String dbValue;


	/**
	 * Erstellt einen neuen OAuth-Server-Modus.
	 *
	 * @param dbValue   der DB-Wert des Modus
	 */
	OAuthServerMode(final @NotNull String dbValue) {
		this.dbValue = dbValue;
	}

	/**
	 * Gibt den DB-Wert des Enums zurück.
	 *
	 * @return den DB-Wert
	 */
	public @NotNull String getDbValue() {
		return this.dbValue;
	}

	/**
	 * Diese Methode ermittelt den OAuth-Server-Modus anhand des übergebenen DB-Wertes.
	 *
	 * @param dbValue   der DB-Wert des OAuth-Server-Modus
	 *
	 * @return der OAuth-Server-Modus
	 */
	public static @NotNull OAuthServerMode getByDbValue(final @NotNull String dbValue) {
		return switch (dbValue) {
			case "DEVELOPMENT" -> OAuthServerMode.DEV;
			case "PRODUCTION" -> OAuthServerMode.PROD;
			default -> throw new DeveloperNotificationException("Der OAuth-Server-Modus mit dem DB-Wert " + dbValue + " existiert nicht.");
		};
	}

	/**
	 * Bildet den Modus, in welchem der Server betrieben wird, auf den OAuth-Server-Modus ab:
	 * BETA, ALPHA und DEV werden auf DEV abgebildet, STABLE und null auf PROD. Der Rückfall
	 * auf PROD ist Absicht: Ein Server im Zweifel nutzt nie die Test-Systeme.
	 *
	 * @param serverMode   der Modus, in welchem der Server betrieben wird
	 *
	 * @return der zugehörige OAuth-Server-Modus
	 */
	public static OAuthServerMode getByServerMode(final ServerMode serverMode) {
		if ((serverMode == ServerMode.BETA) || (serverMode == ServerMode.ALPHA) || (serverMode == ServerMode.DEV)) {
			return OAuthServerMode.DEV;
		}
		return OAuthServerMode.PROD;
	}

}
