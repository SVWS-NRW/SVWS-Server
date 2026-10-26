package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * * Dieser Validator führt eine Statistikprüfung auf eine vorhandene Fachrichtung zu einem Lehramt eines Lehrers aus.
 */
public final class ValidatorLplfLehrerPersonaldatenLehramtFachrichtung extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idFachrichtung           die Katalog-ID der Fachrichtung
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorLplfLehrerPersonaldatenLehramtFachrichtung(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<Long> idFachrichtung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_idSchuljahresabschnitt = idSchuljahresabschnitt;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () ->  kontext.getSchuljahrBySchuljahresabschnitt(getNotNullSupplierLong(idSchuljahresabschnitt));

		_validatoren.add(new ValidatorLplf00LehrerPersonaldatenLehramtFachrichtung(schuljahr, idFachrichtung, kontext));

	}

	@Override
	protected boolean pruefe() {

		final Schuljahresabschnitt sja =  kontext().getSchuljahresabschnittByID(getNotNullSupplierLong(_idSchuljahresabschnitt).get());
		if (sja == null) {
			addFehler(0, "Dem Lehrer ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}

		return true;
	}

}
