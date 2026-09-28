package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Zuzugsjahr des Schülers angegeben ist,
 * obwohl kein ein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	/** Das Zuzugjahr des Schülers */
	private final @NotNull Supplier<@AllowNull Integer> _zuzugsjahr;

	private static final @NotNull String FEHLERTEXT =
			"Das Feld 'Zuzugsjahr' darf nur ausgefüllt werden, wenn ein Migrationshintergrund vorhanden ist.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr(
			final @NotNull Supplier<@AllowNull Integer> zuzugsjahr,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_zuzugsjahr = zuzugsjahr;
	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Integer zuzugsjahr = _zuzugsjahr.get();

		if (zuzugsjahr != null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
