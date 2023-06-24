package de.svws_nrw.core.types;

import jakarta.validation.constraints.NotNull;

/**
 * Dieser Core-Type zählt die unterschiedlichen Server-Modi auf, welche vom
 * Server und Client unterstützt werden.
 */
public enum ServerMode {

	/** STABLE: Es werden nur Funktionen angeboten, die als stabil und getestet angesehen werden */
	STABLE("stable"),

	/** BETA: Es werden auch Funktionen angeboten, welche als weitgehend stabil angesegen werden, sich aber noch in der abschließenden Entwicklung befinden */
	BETA("beta"),

	/** ALPHA: Es werden auch Funktionen angeboten, welche noch nicht als stabil gelten, aber zumindest weitgehend implementiert sind */
	ALPHA("alpha"),

	/** DEV: Es werden alle Funktionen angeboten, auch wenn diese noch nicht stabil sind. */
	DEV("dev");

	/** Der Server-Mode als Text, welche in textuellen Konfigurationen genutzt werden kann */
	public final @NotNull String text;

	/**
	 * Erzeugt einen neuen Modus für die Aufzählung.
	 *
	 * @param text   der Text für den Modus
	 */
	ServerMode(final @NotNull String text) {
		this.text = text;
	}

	@Override
	public @NotNull String toString() {
		return text;
	}

	/**
	 * Liefert das {@link ServerMode}-Objekt anhand des übergebenen Textes.
	 * Der Vergleich des Textes erfolgt dabei case-insensitive. Ist der übergebene
	 * Wert ungültig, so wird als Default STABLE zurückgegeben, so dass ein
	 * Rückgabewert für die Methode garantiert werden kann.
	 *
	 * @param text   Der Text des Server-Modes.
	 *
	 * @return das {@link ServerMode}-Objekt anhand des übergebenen Textes.
	 */
	public static @NotNull ServerMode getByText(final String text) {
		for (final @NotNull ServerMode mode : values())
			if (mode.text.equalsIgnoreCase(text))
				return mode;
	    return ServerMode.STABLE;
	}

}
