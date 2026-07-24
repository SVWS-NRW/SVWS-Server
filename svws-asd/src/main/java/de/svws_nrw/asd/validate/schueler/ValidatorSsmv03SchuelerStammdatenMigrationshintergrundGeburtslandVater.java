package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Geburtsland des Vaters leer ist,
 * wenn kein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/** Geburtsland des Vaters */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandVater;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Geburtsland des Vaters: Das Feld 'Geburtsland Vater' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmv03SchuelerStammdatenMigrationshintergrundGeburtslandVater(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandVater = idGeburtslandVater;
		_hatMigrationshintergrund = hatMigrationshintergrund;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idGeburtslandVater = _idGeburtslandVater.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final boolean hatMigrationshintergrund = (hatMigrationshintergrundZwisch != null) && hatMigrationshintergrundZwisch;

		// Wenn kein Migrationshintergrund vorliegt, darf das Geburtsland nicht gesetzt sein
		if (!hatMigrationshintergrund && (idGeburtslandVater != null)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
