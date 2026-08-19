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
	 * @param idGeburtslandMutter       die ID des Geburtslandes der Mutter
	 * @param idGeburtslandVater        die ID des Geburtslandes des Vaters
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param idStaatsangehoerigkeit    Staatsangehörigkeit
	 * @param idStaatsangehoerigkeit2   Staatsangehörigkeit2
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsSchuelerStammdaten(
			final @NotNull Supplier<@AllowNull Integer> geschlecht,
			final @NotNull Supplier<@AllowNull String> geburtsdatum,
			final @NotNull Supplier<@AllowNull Long> idGeburtsland,
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit2,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorSsgSchuelerStammdatenGeschlecht(geschlecht, kontext));
		_validatoren.add(new ValidatorSsdSchuelerStammdatenGeburtsdatum(geburtsdatum, kontext));
		_validatoren.add(new ValidatorSsmSchuelerStammdatenMigrationshintergrund(idGeburtsland, idGeburtslandMutter, idGeburtslandVater, hatMigrationshintergrund, kontext));
		_validatoren.add(new ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit(idStaatsangehoerigkeit, kontext));
		_validatoren.add(new ValidatorSszsSchuelerStammdatenZweiteStaatsangehoerigkeit(idStaatsangehoerigkeit2, idStaatsangehoerigkeit, kontext));
	}

	@Override
	protected boolean pruefe() {
		// Keine speziellen Prüfungen direkt auf diesem DTO...
		return true;
	}

}
