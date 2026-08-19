package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsland eines Schülers aus.
 */

public final class ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/** Geburtsland */
	private final @NotNull Supplier<@NotNull Long> _idGeburtsland;
	/** hat Migrationshintergrund */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;
	private static final @NotNull String FEHLERTEXT = "Geburtsland des Schülers: Das Feld 'Geburtsland' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland(
			final @NotNull Supplier<@NotNull Long> idGeburtsland,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtsland = idGeburtsland;
		_hatMigrationshintergrund = hatMigrationshintergrund;
	}


	@Override
	protected boolean pruefe() {
		// Bestimme Geburtsland.
		final @NotNull Long idGeburtsland = _idGeburtsland.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final @NotNull Boolean hatMigrationshintergrund = hatMigrationshintergrundZwisch == null ? false : hatMigrationshintergrundZwisch;

		if (!hatMigrationshintergrund) {
			if (idGeburtsland != -1L) {
				addFehler(0, FEHLERTEXT);
				return false;
			}
		}
		return true;
	}
}
