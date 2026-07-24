package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Geburtsland des Vaters angegeben ist,
 * wenn ein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/** Geburtsland des Vaters */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandVater;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland des Vaters: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Geburtsland Vater' besetzt sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater         die ID des Geburtslandes der Vater
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandVater = idGeburtslandVater;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsmv01SchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandVater = _idGeburtslandVater.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final boolean hatMigrationshintergrund = (hatMigrationshintergrundZwisch != null) && hatMigrationshintergrundZwisch;

		if (hatMigrationshintergrund && (idGeburtslandVater == null)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
