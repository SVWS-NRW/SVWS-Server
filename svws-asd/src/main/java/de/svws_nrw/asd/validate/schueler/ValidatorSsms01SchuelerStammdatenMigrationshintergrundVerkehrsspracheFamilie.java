package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Verkehrssprache;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob die angegebene Verkehrssprache der Familie
 * ein gültiger und zulässiger Eintrag im Verkehrssprachen-Katalog ist.
 */
public final class ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	/** Die ID der Verkehrssprache der Familie des Schülers */
	private final @NotNull Supplier<@AllowNull Long> _idVerkehrsspracheFamilie;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Verkehrssprache: Das Feld muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idVerkehrsspracheFamilie  die Katalog-ID der Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsms01SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
			final @NotNull Supplier<@AllowNull Long> idVerkehrsspracheFamilie,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idVerkehrsspracheFamilie = idVerkehrsspracheFamilie;
		_hatMigrationshintergrund = hatMigrationshintergrund;

		_validatoren.add(
				new ValidatorSsms02SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(idVerkehrsspracheFamilie, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Long verkehrsspracheFamilie = _idVerkehrsspracheFamilie.get();

		if (verkehrsspracheFamilie == null) {
			return true;
		}

		// Prüfe, ob die ID im Core-Type-Katalog für Verkehrssprachen existiert
		if (Verkehrssprache.data().getWertByIDOrNull(verkehrsspracheFamilie) == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
