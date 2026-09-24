package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Staatsangehörigkeit bei den Stammdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;
	private final @NotNull ValidatorKontext _kontextNeu;

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Staatsangehörigkeit.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idStaatsangehoerigkeit   StaatsangehörigkeitID
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorSsesSchuelerStammdatenErsteStaatsangehoerigkeit(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idStaatsangehoerigkeit,
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
		_validatoren.add(new ValidatorSses00SchuelerStammdatenErsteStaatsangehoerigkeit(schuljahr, getNotNullSupplierLong(idStaatsangehoerigkeit), kontext));
	}

	@Override
	protected boolean pruefe() {

		final Schuljahresabschnitt sja =  _kontextNeu.getSchuljahresabschnittByID(getNotNullSupplierLong(_idSchuljahresabschnitt).get());
		if (sja == null) {
			addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}

		return true;
	}

}
