package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob der angegebene Einsatzstatus existiert.
 */
public final class ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@NotNull Long> _idEinsatzstatus;
	private static final @NotNull String FEHLERTEXT = "Lehrer Einsatzstatus: Das Feld 'Einsatzstatus' muss zulässig sein.";

	/**
	 * Erstellt einen neuen Validator für das vorhandensein des Einsatzstatus im Katalog.
	 *
	 * @param idEinsatzstatus   der Einsatzstatus
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(
			final @NotNull Supplier<@NotNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEinsatzstatus = idEinsatzstatus;


		_validatoren.add(
				new ValidatorLppe02LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(getNotNullSupplierLong(idEinsatzstatus),
						kontext));

	}

	@Override
	protected boolean pruefe() {
		// Bestimme den Einsatzstatus.
		final Long idEinsatzstatus = _idEinsatzstatus.get();

		if (LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatzstatus) == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
