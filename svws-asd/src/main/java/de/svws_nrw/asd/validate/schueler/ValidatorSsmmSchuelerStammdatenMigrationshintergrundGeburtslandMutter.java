package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Prüfung auf das Geburtsland der Mutter
 * im Bereich Migrationshintergrund der Schülerstammdaten aus.
 */
public final class ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter extends Validator {

	/** Geburtsland der Mutter */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandMutter;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandMutter        die ID des Geburtslandes der Mutter
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmmSchuelerStammdatenMigrationshintergrundGeburtslandMutter(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandMutter,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandMutter = idGeburtslandMutter;

		_validatoren.add(
				new ValidatorSsmm00SchuelerStammdatenMigrationshintergrundGeburtslandMutter(_idGeburtslandMutter, hatMigrationshintergrund, kontext));

	}

	@Override
	protected boolean pruefe() {
		return true;
	}
}
