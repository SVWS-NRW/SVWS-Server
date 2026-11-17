package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf das Pflichtstundensoll in Kombination
 * mit dem Einsatzstatus und der Beschäftigungsart der Personalabschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll des Lehrers
	 * @param idEinsatzstatus        die Katalog-ID des Einsatzstatus des Lehrers
	 * @param idBeschaeftigungsart   die Katalog-ID der Beschäftigungsart des Lehrers
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLpppkLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);

		_validatoren.add(new ValidatorLpppk10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensollKombination(pflichtstundensoll, idEinsatzstatus, idBeschaeftigungsart,
				kontext));

	}

	@Override
	protected boolean pruefe() {

		return true;
	}

}
