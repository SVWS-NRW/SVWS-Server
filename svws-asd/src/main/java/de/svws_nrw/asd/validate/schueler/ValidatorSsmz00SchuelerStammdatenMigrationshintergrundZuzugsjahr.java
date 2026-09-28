package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator prüft, ob das Zuzugsjahr des Schülers angegeben ist,
 * wenn ein Migrationshintergrund beim Schüler vorhanden ist.
 */
public final class ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {




	/** Das Zuzugjahr des Schülers */
	private final @NotNull Supplier<@AllowNull Integer> _zuzugsjahr;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	/** Das Schuljahr */
	private @NotNull Supplier<@AllowNull Integer> _schuljahr;

	/** Das Geburtsdatum des Schülers */
	private @NotNull Supplier<String> _geburtsdatum;

	/** Der Kontext */
	private @NotNull ValidatorKontext _kontextNeu;

	private static final @NotNull String FEHLERTEXT =
			"Zuzugsjahr des Schülers: Wenn ein Migrationshintergrund vorhanden ist, muss das Feld 'Zuzugsjahr' besetzt sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param schuljahr                 das Schuljahr des Schülers
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum              das Geburtsdatum des Schülers
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr(
			final @NotNull Supplier<@AllowNull Integer> schuljahr,
			final @NotNull Supplier<@AllowNull Integer> zuzugsjahr,
			final @NotNull Supplier<String> geburtsdatum,
			final @NotNull Supplier<@AllowNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_schuljahr = schuljahr;
		_zuzugsjahr = zuzugsjahr;
		_geburtsdatum = geburtsdatum;
		_hatMigrationshintergrund = hatMigrationshintergrund;
		_kontextNeu = kontext;

	}

	@Override
	protected boolean pruefe() {
		final @AllowNull Integer zuzugsjahr = _zuzugsjahr.get();
		final @AllowNull Boolean hatMigrationshintergrundZwisch = _hatMigrationshintergrund.get();
		final boolean hatMigrationshintergrund = (hatMigrationshintergrundZwisch != null) && hatMigrationshintergrundZwisch;

		if (hatMigrationshintergrund) {
			if (zuzugsjahr == null) {
				addFehler(0, FEHLERTEXT);
				return false;
			}
			_validatoren.add(
					new ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr(_schuljahr, () -> zuzugsjahr, _geburtsdatum, () -> hatMigrationshintergrund, _kontextNeu));
		} else {
			_validatoren.add(
					new ValidatorSsmz12SchuelerStammdatenMigrationshintergrundZuzugsjahr(() -> zuzugsjahr, _kontextNeu));
		}

		return true;
	}
}
