package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der eingetragene Wert für das Feld 'Geburtsland Vater'
 * für das ausgewählte Schuljahr gültig ist.
 */
public final class ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/** Geburtsland des Vaters */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandVater;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland des Vaters: Der eingetragene Wert für das Feld 'Geburtsland Vater' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandVater = idGeburtslandVater;

		_validatoren.add(
				new ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));

	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandVater = _idGeburtslandVater.get();

		if (idGeburtslandVater == null) {
			return true;
		}

		final int schuljahr = kontext().getSchuljahr();
		if (!Nationalitaeten.data().isGueltig(idGeburtslandVater, schuljahr)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
