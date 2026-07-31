package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob die Verkehrssprache der Familie leer ist,
 * wenn kein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/** Die ID der Verkehrssprache der Familie des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _idVerkehrsspracheFamilie;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Verkehrssprache: Das Feld 'Verkehrssprache' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie  die ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsms03SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
			final @NotNull Supplier<@AllowNull Long> idVerkehrsspracheFamilie,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idVerkehrsspracheFamilie = idVerkehrsspracheFamilie;
		_hatMigrationshintergrund = hatMigrationshintergrund;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long idVerkehrsspracheFamilie = _idVerkehrsspracheFamilie.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final boolean hatMigrationshintergrund = (hatMigrationshintergrundZwisch != null) && hatMigrationshintergrundZwisch;

		// Wenn kein Migrationshintergrund vorliegt, darf die Verkehrssprache nicht gesetzt sein
		if (!hatMigrationshintergrund && (idVerkehrsspracheFamilie != null)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
