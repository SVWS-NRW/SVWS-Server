package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob die Verkehrssprache der Familie angegeben ist,
 * wenn ein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/** Die Verkehrssprache der Familie des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _idVerkehrsspracheFamilie;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Verkehrssprache: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Verkehrssprache' besetzt sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param verkehrsspracheFamilie    die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
			final @NotNull Supplier<@AllowNull Long> verkehrsspracheFamilie,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idVerkehrsspracheFamilie = verkehrsspracheFamilie;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(verkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long verkehrsspracheFamilie = _idVerkehrsspracheFamilie.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final boolean hatMigrationshintergrund = (hatMigrationshintergrundZwisch != null) && hatMigrationshintergrundZwisch;

		if (hatMigrationshintergrund && (verkehrsspracheFamilie == null)) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
