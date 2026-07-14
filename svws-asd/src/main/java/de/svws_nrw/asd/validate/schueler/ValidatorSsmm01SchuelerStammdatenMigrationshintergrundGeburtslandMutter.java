package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das angegebene Geburtsland der Mutter
 * ein gültiger und zulässiger Eintrag im Nationalitäten-Katalog ist.
 */
public final class ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/** Geburtsland der Mutter */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandMutter;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland der Mutter: Das Feld muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandMutter = idGeburtslandMutter;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsmm02SchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandMutter = _idGeburtslandMutter.get();

		if (idGeburtslandMutter == null) {
			return true;
		}

		// Prüfe, ob die ID im Core-Type-Katalog für Nationalitäten existiert
		if (Nationalitaeten.data().getWertByIDOrNull(idGeburtslandMutter) == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
