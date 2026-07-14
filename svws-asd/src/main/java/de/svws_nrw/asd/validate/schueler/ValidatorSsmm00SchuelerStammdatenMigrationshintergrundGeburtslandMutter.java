package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Geburtsland der Mutter angegeben ist,
 * wenn ein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsmm00SchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/** Geburtsland der Mutter */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandMutter;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland der Mutter: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Geburtsland Mutter' besetzt sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmm00SchuelerStammdatenMigrationshintergrundGeburtslandMutter(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandMutter = idGeburtslandMutter;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsmm01SchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandMutter = _idGeburtslandMutter.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final boolean hatMigrationshintergrund = (hatMigrationshintergrundZwisch != null) && hatMigrationshintergrundZwisch;

		if (hatMigrationshintergrund && (idGeburtslandMutter == null)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
