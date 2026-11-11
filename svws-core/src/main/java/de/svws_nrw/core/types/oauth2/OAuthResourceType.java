package de.svws_nrw.core.types.oauth2;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Die Arten von Resource Servern, die über OAuth angebunden werden.
 */
public enum OAuthResourceType {

	/** Der Signierdienst von IT.NRW */
	IT_NRW_SIGNIERDIENST("SIGNIERDIENST"),

	/** Der Abruf von Daten (z.B. erfolgreiche Bewerbungen) bei schulbewerbung.de */
	SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN("ERFOLGREICHE_BEWERBUNGEN");


	private final @NotNull String dbValue;


	/**
	 * Erstellt einen neuen OAuth-Ressourcentyp.
	 *
	 * @param dbValue   der DB-Wert des Ressourcentyps
	 */
	OAuthResourceType(final @NotNull String dbValue) {
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
	 * Diese Methode ermittelt den OAuth-Ressourcentyp anhand des übergebenen DB-Wertes.
	 *
	 * @param dbValue   der DB-Wert des OAuth-Ressourcentyps
	 *
	 * @return der OAuth-Ressourcentyp
	 */
	public static @NotNull OAuthResourceType getByDbValue(final @NotNull String dbValue) {
		return switch (dbValue) {
			case "SIGNIERDIENST" -> OAuthResourceType.IT_NRW_SIGNIERDIENST;
			case "ERFOLGREICHE_BEWERBUNGEN" -> OAuthResourceType.SCHULBEWERBUNG_ERFOLGREICHE_BEWERBUNGEN;
			default -> throw new DeveloperNotificationException("Der OAuth-Ressourcentyp mit dem DB-Wert " + dbValue + " existiert nicht.");
		};
	}

}
