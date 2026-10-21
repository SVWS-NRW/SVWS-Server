package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Schulgliederung bei den Lernabschnittsdaten
 * eines Schuelers einer Schule aus.
 */
public final class ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchuljahresabschnitt;

	/**
	 * Erstellt einen neuen Validator für die Prüfung der Schulgliederung.
	 *
	 * @param idSchuljahresabschnitt   Schuljahresabschnitt ID
	 * @param idSchulgliederung        die ID der Schulgliederung
	 * @param kontext                  der Kontext des Validators
	 */
	public ValidatorSlsSchuelerLernabschnittsdatenSchulgliederung(
			final @NotNull Supplier<@AllowNull Long> idSchuljahresabschnitt,
			final @NotNull Supplier<@AllowNull Long> idSchulgliederung,
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
		_validatoren.add(new ValidatorSls00SchuelerLernabschnittsdatenSchulgliederung(schuljahr, idSchulgliederung, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Schuljahresabschnitt sja =  kontext().getSchuljahresabschnittByID(getNotNullSupplierLong(_idSchuljahresabschnitt).get());
		if (sja == null) {
			addFehler(0, "Dem Schüler ist kein Schuljahresabschnitt zugeordnet");
			return false;
		}

		return true;
	}

}
