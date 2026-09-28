package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Prüfung auf das Zuzugsjahr des Schülers
 * im Bereich Migrationshintergrund der Schülerstammdaten aus.
 */
public final class ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;
	private final @NotNull ValidatorKontext _kontextNeu;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
     * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param zuzugsjahr                das Zuzugsjahr des Schülers
	 * @param geburtsdatum             das Geburtsjahr des Schülers
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmzSchuelerStammdatenMigrationshintergrundZuzugsjahr(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Integer> zuzugsjahr,
			final @NotNull Supplier<String> geburtsdatum,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_idSchuljahresabschnitt = idSchuljahresabschnitt;
		_kontextNeu = kontext;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () -> {
			final @AllowNull Schuljahresabschnitt sja =  _kontextNeu.getSchuljahresabschnittByID(getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja == null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		};

		_validatoren.add(
				new ValidatorSsmz00SchuelerStammdatenMigrationshintergrundZuzugsjahr(schuljahr, zuzugsjahr, geburtsdatum, hatMigrationshintergrund,
						kontext));



	}

	@Override
	protected boolean pruefe() {
		return true;
	}
}
