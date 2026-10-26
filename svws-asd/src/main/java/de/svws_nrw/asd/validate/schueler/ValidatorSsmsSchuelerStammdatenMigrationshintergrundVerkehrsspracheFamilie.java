package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Prüfung auf die Verkehrssprache in der Familie
 * im Bereich Migrationshintergrund der Schülerstammdaten aus.
 */
public final class ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt    Schuljahresabschnitt ID
	 * @param idVerkehrsspracheFamilie  die Verkehrssprache der Familie
	 * @param hatMigrationshintergrund  Gibt an, ob ein Migrationshintergrund vorhanden ist
	 * @param kontext                   der Kontext des Validators
	 */
	public ValidatorSsmsSchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idVerkehrsspracheFamilie,
			final @NotNull Supplier<@NotNull Boolean> hatMigrationshintergrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_idSchuljahresabschnitt = idSchuljahresabschnitt;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () ->  kontext.getSchuljahrBySchuljahresabschnitt(getNotNullSupplierLong(idSchuljahresabschnitt));

		_validatoren.add(
				new ValidatorSsms00SchuelerStammdatenMigrationshintergrundVerkehrsspracheFamilie(schuljahr, idVerkehrsspracheFamilie, hatMigrationshintergrund,
						kontext)
		);
	}

	@Override
	protected boolean pruefe() {
		return true;
	}
}
