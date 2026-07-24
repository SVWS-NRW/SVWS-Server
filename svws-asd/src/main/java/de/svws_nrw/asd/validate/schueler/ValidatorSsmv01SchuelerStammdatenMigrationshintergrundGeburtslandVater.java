package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das angegebene Geburtsland des Vaters
 * ein gültiger und zulässiger Eintrag im Nationalitäten-Katalog ist.
 */
public final class ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/** Geburtsland des Vaters */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandVater;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland des Vaters: Das Feld muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandVater = idGeburtslandVater;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsmv02SchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandVater = _idGeburtslandVater.get();

		if (idGeburtslandVater == null) {
			return true;
		}

		// Prüfe, ob die ID im Core-Type-Katalog für Nationalitäten existiert
		if (Nationalitaeten.data().getWertByIDOrNull(idGeburtslandVater) == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
