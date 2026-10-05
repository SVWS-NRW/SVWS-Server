package de.svws_nrw.core.types.schule;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse enthält die zulässigen Status eines Wechselvorgangs nach der erfolgreichen Bewerbung eines Schülers.
 */
public enum StatusSchulwechselErfolgreicheBewerbung {

	/** Initialer Status, nachdem ein Schüler von schulbewerbung.de importiert wurde */
	NEU(1, "neu"),
	/** Wird gesetzt, sobald ein ordentlicher Datensatz für den Schüler angelegt wurde */
	AUFGENOMMEN(2, "aufgenommen");

	private final int id;

	private final @NotNull String bezeichnung;

	StatusSchulwechselErfolgreicheBewerbung(final int id, final @NotNull String bezeichnung) {
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
	public static StatusSchulwechselErfolgreicheBewerbung getByIdOrNull(final int id) {
		for (final @NotNull StatusSchulwechselErfolgreicheBewerbung status : StatusSchulwechselErfolgreicheBewerbung.values()) {
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
	public static StatusSchulwechselErfolgreicheBewerbung getByBezeichnungOrNull(final @NotNull String bezeichnung) {
		for (final @NotNull StatusSchulwechselErfolgreicheBewerbung status : StatusSchulwechselErfolgreicheBewerbung.values()) {
			if (status.bezeichnung.equals(bezeichnung)) {
				return status;
			}
		}
		return null;
	}

	/**
	 * Gibt die Bezeichnung des Status zurück.
	 *
	 * @return die Bezeichnung des Status
	 */
	public @NotNull String getBezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt die Id des Status zurück.
	 *
	 * @return die Id des Status
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gibt den Statustext zurück.
	 *
	 * @return den Text des Status
	 */
	@Override
	public @NotNull String toString() {
		return getBezeichnung();
	}
}
