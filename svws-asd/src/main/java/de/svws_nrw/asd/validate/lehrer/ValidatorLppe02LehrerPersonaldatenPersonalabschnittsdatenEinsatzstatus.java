package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der angegebene Einsatzstatus existiert.
 */
public final class ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@NotNull Long> _idEinsatzstatus;

	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param idEinsatzstatus   der Einsatzstatus
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(
			final @NotNull Supplier<@NotNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEinsatzstatus = idEinsatzstatus;


	}

	@Override
	protected boolean pruefe() {
		if (!LehrerEinsatzstatus.data().isGueltig(_idEinsatzstatus.get(), kontext().getSchuljahr())) {
			addFehler(0,
					"Lehrer Einsatzstatus: Der eingetragene Wert für das Feld 'Einsatzstatus' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}
}
