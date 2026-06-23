package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsland eines Schülers aus.
 */

public final class ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/** Das Geburtsland */
	private final @NotNull Supplier<@NotNull Long> _idGeburtsland;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland des Schülers: Der eingetragene Wert für das Feld 'Geburtsland' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator für die Existenzprüfung der Anrechnungsgründe im Katalog.
	 *
	 * @param idGeburtsland     die ID des Geburtslands
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorSsml02SchuelerStammdatenMigrationshintergrundGeburtsland(
			final @NotNull Supplier<@NotNull Long> idGeburtsland,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtsland = idGeburtsland;

	}

	@Override
	protected boolean pruefe() {
		if (!Nationalitaeten.data().isGueltig(_idGeburtsland.get(), kontext().getSchuljahr())) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}

}
