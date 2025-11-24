package de.svws_nrw.asd.validate.gesamt;

import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
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
	 * @param listPersonaldaten   die Liste der Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorGlplGesamtLehrerPersonaldatenLehramt(final @NotNull List<LehrerPersonaldaten> listPersonaldaten, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorGlpl00GesamtLehrerPersonaldatenLehramt(listPersonaldaten, kontext));
		_validatoren.add(new ValidatorGlpl01GesamtLehrerPersonaldatenLehramt(listPersonaldaten, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
