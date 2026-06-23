package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsSchuelerStammdaten extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param geschlecht                das geschlecht des Schuelers
	 * @param geburtsdatum              das geburtsdatum des Schuelers
	 * @param idGeburtsland             die ID des Geburtslandes
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsSchuelerStammdaten(
			final @NotNull Supplier<@AllowNull Integer> geschlecht,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<@AllowNull Long> idGeburtsland,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSsgSchuelerStammdatenGeschlecht(geschlecht, kontext));
		_validatoren.add(new ValidatorSsdSchuelerStammdatenGeburtsdatum(geburtsdatum, kontext));
		_validatoren.add(new ValidatorSsmSchuelerStammdatenMigrationshintergrund(idGeburtsland, hatMigrationshintergrund, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
