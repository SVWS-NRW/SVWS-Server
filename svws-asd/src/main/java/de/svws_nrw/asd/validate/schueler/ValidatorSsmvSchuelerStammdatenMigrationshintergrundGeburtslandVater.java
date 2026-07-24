package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Prüfung auf das Geburtsland des Vater
 * im Bereich Migrationshintergrund der Schülerstammdaten aus.
 */
public final class ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater extends Validator {

	/** Geburtsland des Vaters */
	private final @NotNull Supplier<@AllowNull Long> _idGeburtslandVater;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idGeburtslandVater        die ID des Geburtslandes der Vater
	 * @param hatMigrationshintergrund   Migrationshintergrund vorhanden
	 * @param kontext                    der Kontext des Validators
	 */
	public ValidatorSsmvSchuelerStammdatenMigrationshintergrundGeburtslandVater(
			final @NotNull Supplier<@AllowNull Long> idGeburtslandVater,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idGeburtslandVater = idGeburtslandVater;

		_validatoren.add(
				new ValidatorSsmv00SchuelerStammdatenMigrationshintergrundGeburtslandVater(_idGeburtslandVater, hatMigrationshintergrund, kontext));

	}

	@Override
	protected boolean pruefe() {
		return true;
	}
}
