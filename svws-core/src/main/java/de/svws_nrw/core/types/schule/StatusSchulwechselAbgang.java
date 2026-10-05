package de.svws_nrw.core.types.schule;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält die zulässigen Status eines Wechselvorgangs beim Abgang eines Schülers.
 */
public enum StatusSchulwechselAbgang {

	/** Initialer Status, wenn ein Schüler für einen Wechsel markiert wurde */
	BEVORSTEHEND(1, "bevorstehend"),
	/** Wird gesetzt, sobald der Versand des Wechseldokuments eines Schülers angestossen wurde */
	GEPLANT(2, "geplant"),
	/** Wird gesetzt, sobald ein Wechseldokument für den Wechselvorgang versendet wurde */
	GESENDET(3, "gesendet"),
	/** Wird gesetzt, sobald die Bestätigung für den erfolgreichen Wechselvorgang eingegangen ist */
	BESTAETIGT(4, "bestätigt"),
	/** Wird gesetzt, wenn ein Wechsel nicht erfolgreich war */
	NICHT_VERSORGT(5, "nicht versorgt");

	private final int id;

	private final @NotNull String bezeichnung;

	StatusSchulwechselAbgang(final int id, final @NotNull String bezeichnung) {
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Bestimmt den Status anhand der Id.
	 *
	 * @param id   die Id des Status
	 *
	 * @return den Status oder null, falls kein Status mit dieser Id existiert
	 */
	public static StatusSchulwechselAbgang getByIdOrNull(final @NotNull int id) {
		for (final @NotNull StatusSchulwechselAbgang status : StatusSchulwechselAbgang.values()) {
			if (status.id == id) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Bestimmt den Status anhand der Bezeichnung.
	 *
	 * @param bezeichnung   die Bezeichnung des Status
	 *
	 * @return den Status oder null, falls kein Status mit dieser Bezeichnung existiert
	 */
	public static StatusSchulwechselAbgang getByBezeichnungOrNull(final @NotNull String bezeichnung) {
		for (final @NotNull StatusSchulwechselAbgang status : StatusSchulwechselAbgang.values()) {
			if (status.bezeichnung.equals(bezeichnung)) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gibt den Statustext zurück.
	 *
	 * @return den Text des Status
	 */
	@Override
	public @NotNull String toString() {
		return this.bezeichnung;
	}

	/**
	 * Gibt die Id des Status zurück.
	 *
	 * @return die Id des Status
	 */
	public int getId() {
		return id;
	}
}
