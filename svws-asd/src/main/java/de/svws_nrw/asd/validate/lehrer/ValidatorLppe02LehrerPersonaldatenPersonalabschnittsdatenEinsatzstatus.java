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

	/** Das aktuelle Schuljahr des Lehrers */
	private final @NotNull Supplier<@NotNull Integer> _schuljahr;

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@NotNull Long> _idEinsatzstatus;

	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param schuljahr                Schuljahr
	 * @param idEinsatzstatus   der Einsatzstatus
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(
			final @NotNull Supplier<@NotNull Integer> schuljahr,
			final @NotNull Supplier<@NotNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {

		super(kontext);

		_schuljahr = schuljahr;
		_idEinsatzstatus = idEinsatzstatus;


	}

	@Override
	protected boolean pruefe() {
		if (!LehrerEinsatzstatus.data().isGueltig(_idEinsatzstatus.get(), _schuljahr.get())) {
			addFehler(0,
					"Lehrer Einsatzstatus: Der eingetragene Wert für das Feld 'Einsatzstatus' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}

		return true;
	}
}
