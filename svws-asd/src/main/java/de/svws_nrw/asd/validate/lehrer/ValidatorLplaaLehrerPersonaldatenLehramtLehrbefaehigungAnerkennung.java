package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idAnerkennungsgrund   die Katalog-ID des Anerkennungsgrunds
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplaaLehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idAnerkennungsgrund,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_idSchuljahresabschnitt = idSchuljahresabschnitt;

		final @NotNull Supplier<@NotNull Integer> schuljahr = () -> {
			final @AllowNull Schuljahresabschnitt sja =  kontext.getSchuljahresabschnittByID(getNotNullSupplierLong(idSchuljahresabschnitt).get());
			if (sja == null) {
				throw new NullPointerException();
			}
			return sja.schuljahr;
		};

		_validatoren.add(new ValidatorLplaa00LehrerPersonaldatenLehramtLehrbefaehigungAnerkennung(schuljahr, idAnerkennungsgrund, kontext));
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
