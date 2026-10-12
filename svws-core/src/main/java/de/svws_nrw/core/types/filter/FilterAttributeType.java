package de.svws_nrw.core.types.filter;

import jakarta.validation.constraints.NotNull;

/**
 * Die Datentypen, die ein Filterattribut haben kann. Der Typ bestimmt, wie der zu filternde Wert dargestellt und ermittelt wird.
 */
public enum FilterAttributeType {
	/** Zeichenkette */
	STRING("string"),
	/** Zahl */
	NUMBER("number"),
	/** Datum */
	DATE("date"),
	/** Wahrheitswert */
	BOOLEAN("boolean"),
	/** Wert aus einem Enum */
	ENUM("enum"),
	/** Wert aus einem CoreType */
	CORETYPE("coretype"),
	/** Wert aus einem Katalog */
	CATALOG("catalog");


	/** Ein eindeutiger Schlüssel zur Identifizierung des Enum-Eintrags */
	private final @NotNull String key;

	/**
	 * @param key Ein eindeutiger Schlüssel zur Identifizierung des Enum-Eintrags
	 */
	FilterAttributeType(final @NotNull String key) {
		this.key = key;
	}

	/**
	 * Liefert den Schlüssel zurück.
	 *
	 * @return Schlüssel
	 */
	public String getKey() {
		return this.key;
	}
}
