package de.svws_nrw.asd.validate.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

public final class ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell extends Validator {

	/** Die Lehrer-Personalabschnittsdaten, die geprüft werden. */
	private final @NotNull LehrerPersonalabschnittsdaten daten;

	/**
	 * Erstellt einen neuen Validator.
	 *
	 * @param daten    die Abschnittsdaten der Lehrkraft
	 * @param kontext  der Kontext der Validierung
	 */
	public ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(
			final @NotNull LehrerPersonalabschnittsdaten daten,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.daten = daten;
		_validatoren.add(new ValidatorLppbb01LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(daten, kontext));

	}

	@Override
	protected boolean pruefe() {

		return true;
	}
}
