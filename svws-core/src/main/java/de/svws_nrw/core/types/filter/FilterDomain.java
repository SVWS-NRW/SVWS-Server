package de.svws_nrw.core.types.filter;

import jakarta.validation.constraints.NotNull;

/**
 * Die Fachdomänen, für die Filterkategorien und Filterattribute bereitgestellt werden.
 */
public enum FilterDomain {
	/** Schüler */
	SCHUELER("schueler");


	/** Ein eindeutiger Schlüssel zur Identifizierung der Domäne, z. B. in URLs */
	private final @NotNull String key;

	/**
	 * Erzeugt eine neue Domäne für die Aufzählung.
	 *
	 * @param key Ein eindeutiger Schlüssel zur Identifizierung der Domäne
	 */
	FilterDomain(final @NotNull String key) {
		this.key = key;
	}

	/**
	 * Liefert den Schlüssel der Domäne zurück.
	 *
	 * @return Schlüssel
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Liefert die Domäne zu dem übergebenen Schlüssel zurück. Der Vergleich erfolgt ohne Beachtung der Groß- und Kleinschreibung.
	 *
	 * @param key der Schlüssel der Domäne
	 *
	 * @return die Domäne oder {@code null}, wenn es zu dem Schlüssel keine Domäne gibt
	 */
	public static FilterDomain getByKey(final String key) {
		for (final FilterDomain domain : values()) {
			if (domain.key.equalsIgnoreCase(key)) {
				return domain;
			}
		}
		return null;
	}
}
