package de.svws_nrw.asd.validate.lehrer;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Dieser Validator führt eine Statistikprüfung auf den Einsatzstatus der Abschnittsdaten
 * eines Lehrers einer Schule aus.
 */
public final class ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus extends Validator {

	/** Der Einsatzstatus */
	private final @NotNull Supplier<@AllowNull Long> _idEinsatzstatus;
	private static final @NotNull String FEHLERTEXT = "Lehrer Einsatzstatus: Das Feld darf nicht leer sein.";

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idEinsatzstatus   die ID des Einsatzstatus.
	 * @param kontext           der Kontext des Validators
	 */
	public ValidatorLppe00LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEinsatzstatus = idEinsatzstatus;

		_validatoren.add(
				new ValidatorLppe01LehrerPersonaldatenPersonalabschnittsdatenEinsatzstatus(getNotNullSupplierLong(idEinsatzstatus),
						kontext));

	}


	@Override
	protected boolean pruefe() {
		// Bestimme den Einsatzstatus.
		final Long idEinsatzstatus = _idEinsatzstatus.get();

		if (idEinsatzstatus == null) {
			addFehler(0, FEHLERTEXT);
			return false;
		}

		return true;
	}
}
