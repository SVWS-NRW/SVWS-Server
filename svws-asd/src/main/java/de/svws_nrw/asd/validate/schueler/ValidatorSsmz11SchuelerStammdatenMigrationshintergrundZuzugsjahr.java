package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.DateManager;
import de.svws_nrw.asd.validate.InvalidDateException;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Prüfung des Zuzugjahres des Schülers im Bereich Migrationshintergrund durch
 */
public final class ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	/** Das Schuljahr des Schülers */
	private final @NotNull Supplier<@AllowNull Integer> _schuljahr;

	/** Das Zuzugsjahr des Schülers */
	private final @NotNull Supplier<@AllowNull Integer> _zuzugsjahr;

	/** Das Geburtsdatumm des Schülers */
	private final @NotNull Supplier<String> _geburtsdatum;

	/** Gibt an, ob ein Migrationshintergrund vorhanden ist */
	private final @NotNull Supplier<@AllowNull Boolean> _hatMigrationshintergrund;

	private static final @NotNull String FEHLERTEXT =
			"Das eingetragene 'Zuzugsjahr' darf nicht vor dem Geburtsdatum des Schülers und nicht in der Zukunft liegen.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
 	 * @param schuljahr                 das Schuljahr des Schülers
    * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum              das Geburtsdatum des Schülers
	 * @param hatMigrationshintergrund  Migrationshintergrund vorhanden
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmz11SchuelerStammdatenMigrationshintergrundZuzugsjahr(
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

	}



	@Override
	protected boolean pruefe() {
		// Bestimme das Geburtsdatum
		DateManager geburtsdatum = null;
		@NotNull String errorMsg = "";
		try {
			geburtsdatum = DateManager.from(_geburtsdatum.get());
		} catch (final InvalidDateException e) {
			errorMsg = e.getMessage();
		}
		if (geburtsdatum == null) {
			return true;
		}
		final DateManager finalGeburtsdatum = geburtsdatum; //wegen Lambda hier nochmal als final.
		 @NotNull Supplier<Integer> zuzugsjahrNeu = getNotNullSupplierInteger(_zuzugsjahr);
		 @NotNull Supplier<Integer> schuljahrNeu = getNotNullSupplierInteger(_schuljahr);

		if (zuzugsjahrNeu.get() < finalGeburtsdatum.getJahr()  || zuzugsjahrNeu.get() > schuljahrNeu.get()) {
			this.addFehler(0, FEHLERTEXT + errorMsg);
			return false;
		}

		return true;
	}

}
