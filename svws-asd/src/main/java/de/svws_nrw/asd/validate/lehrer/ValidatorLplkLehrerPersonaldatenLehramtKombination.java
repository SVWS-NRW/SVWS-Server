package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine KOmbinationsprüfungen auf die vorhandenen Lehrämter
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLplkLehrerPersonaldatenLehramtKombination extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter            die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public ValidatorLplkLehrerPersonaldatenLehramtKombination(
			final @NotNull Supplier<List<LehrerLehramtEintrag>> lehraemter,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLplk10LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk11LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk12LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk13LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk14LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk15LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk16LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk17LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk18LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk19LehrerPersonaldatenLehramtKombination(lehraemter, kontext));
		_validatoren.add(new ValidatorLplk20LehrerPersonaldatenLehramtKombination(lehraemter, kontext));	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
