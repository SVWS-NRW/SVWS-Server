package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschaeftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorSsmSchuelerStammdatenMigrationshintergrund extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtsland              die ID des Geburtslands
	 * @param idGeburtslandMutter        die ID des Geburtslands der Mutter
	 * @param idGeburtslandVater         die ID des Geburtslands des Vaters
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmSchuelerStammdatenMigrationshintergrund(
			final @NotNull Supplier<@AllowNull Long> idGeburtsland,
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(
				new ValidatorSsmlSchuelerStammdatenMigrationshintergrundGeburtsland(idGeburtsland, hatMigrationshintergrund, kontext));
		_validatoren.add(
				new ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter(idGeburtslandMutter, hatMigrationshintergrund, kontext));
		_validatoren.add(
				new ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater(idGeburtslandVater, hatMigrationshintergrund, kontext));

	}


	@Override
	protected boolean pruefe() {
		return true;
	}
}
