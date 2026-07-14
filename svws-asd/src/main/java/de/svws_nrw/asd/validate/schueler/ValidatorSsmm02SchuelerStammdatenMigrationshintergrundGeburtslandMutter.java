package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob der eingetragene Wert für das Feld 'Geburtsland Mutter'
 * für das ausgewählte Schuljahr gültig ist.
 */
public final class ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/** Geburtsland der Mutter */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandMutter;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland der Mutter: Der eingetragene Wert für das Feld 'Geburtsland Mutter' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandMutter = idGeburtslandMutter;

		_validatoren.add(
				new ValidatorSsmm03SchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));

	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandMutter = _idGeburtslandMutter.get();

		if (idGeburtslandMutter == null) {
			return true;
		}

		final int schuljahr = kontext().getSchuljahr();
		if (!Nationalitaeten.data().isGueltig(idGeburtslandMutter, schuljahr)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
