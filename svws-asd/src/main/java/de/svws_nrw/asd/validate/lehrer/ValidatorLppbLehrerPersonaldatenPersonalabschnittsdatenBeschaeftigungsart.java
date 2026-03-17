package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf die Beschäftigungsart der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart extends Validator {

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idBeschaeftigungsart    die Beschäftigungsart
	 * @param idEinsatzstatus     	der Einsatzstatus
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param kontext   			der Kontext des Validators
	 */
	public ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(
			final @NotNull Supplier<Long> idBeschaeftigungsart,
			final @NotNull Supplier<Long> idEinsatzstatus,
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_validatoren.add(new ValidatorLppb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, idEinsatzstatus, kontext));
		_validatoren.add(new ValidatorLppb11LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(idBeschaeftigungsart, idEinsatzstatus, pflichtstundensoll,
				kontext));

	}


	@Override
	protected boolean pruefe() {
		return true;
	}

}
