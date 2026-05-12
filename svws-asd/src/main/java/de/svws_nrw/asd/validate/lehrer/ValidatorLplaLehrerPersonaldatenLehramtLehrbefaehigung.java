package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf ein vorhandenes Lehramt
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter            die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung(
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
