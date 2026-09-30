package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Nachnamen bei den Stammdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLsrLehrerStammdatenRelevantFuerStatistik extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param istRelevantFuerStatistik     ist relevant für Statistik
	 * @param abgangsdatum                 das Abgangsdatum des Lehrers
	 * @param kontext                      der Kontext des Validators
	 */
	public ValidatorLsrLehrerStammdatenRelevantFuerStatistik(final @NotNull Supplier<@AllowNull Boolean> istRelevantFuerStatistik, final @NotNull Supplier<@AllowNull String> abgangsdatum, final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLsr10LehrerStammdatenRelevantFuerStatistik(istRelevantFuerStatistik, abgangsdatum, kontext));
	}

	@Override
	protected boolean pruefe() {
		return true;
	}

}
