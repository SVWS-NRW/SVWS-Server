package de.svws_nrw.asd.validate.gesamt;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.statistik.LehrerStatistikGesamt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorGlplGesamtLehrerPersonaldatenLehramt extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste der Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGlplGesamtLehrerPersonaldatenLehramt(
			final @NotNull Supplier<List<LehrerStatistikGesamt>> listLehrer,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorGlpl00GesamtLehrerPersonaldatenLehramt(listLehrer, kontext));
		_validatoren.add(new ValidatorGlpl01GesamtLehrerPersonaldatenLehramt(listLehrer, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
