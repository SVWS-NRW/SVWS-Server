package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Geburtsland eines Schülers aus.
 */
public final class ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland extends Validator {

	/** Geburtsland */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtsland;
	/** hat Migrationshintergrund */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;
	private static final @NotNull String FEHLERTEXT = "Geburtsland des Schülers: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Geburtsland' besetzt sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland             die ID des Geburtslands
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsml00SchuelerStammdatenMigrationshintergrundGeburtsland(
			final @NotNull Supplier<@AllowNull Long> idGeburtsland,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtsland = idGeburtsland;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsml01SchuelerStammdatenMigrationshintergrundGeburtsland(getNotNullSupplierLong(idGeburtsland), kontext));
		_validatoren.add(
				new ValidatorSsml03SchuelerStammdatenMigrationshintergrundGeburtsland(getNotNullSupplierLong(idGeburtsland), hatMigrationshintergrund, kontext));
	}


	@Override
	protected boolean pruefe() {
		// Bestimme Geburtsland.
		final @AllowNull Long idGeburtsland = _idGeburtsland.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final @NotNull Boolean hatMigrationshintergrund = hatMigrationshintergrundZwisch == null ? false : hatMigrationshintergrundZwisch;

		if (hatMigrationshintergrund) {
			if (idGeburtsland == null) {
				addFehler(0, FEHLERTEXT);
				return false;
			}
		}
		return true;
	}
}
