package de.svws_nrw.asd.validate.schueler;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob die EP-Jahre zulässig, bzw. in der JSON-Datei vorhanden ist.
 */
public final class ValidatorSle00SchuelerLernabschnittsdatenEpJahre extends Validator {

	/** EP - Jahre */
	private final @NotNull Supplier<@AllowNull Long> _idEpJahre;
	private static final @NotNull String FEHLERTEXT = "EP-Jahr des Schülers: Das Feld 'EP-Jahr' darf nicht leer sein.";

	/**
	 * Erstellt einen neuen Validator zur Überprüfung der EP-Jahre.
	 *
	 * @param idEpJahre   EPJahreID
	 * @param kontext     der Kontext des Validators
	 */
	public ValidatorSle00SchuelerLernabschnittsdatenEpJahre(
			final @NotNull Supplier<@AllowNull Long> idEpJahre,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_idEpJahre = idEpJahre;

		_validatoren.add(new ValidatorSle01SchuelerLernabschnittsdatenEpJahre(getNotNullSupplierLong(idEpJahre), kontext));
	}

	@Override
	protected boolean pruefe() {
		// Bestimme epJahre.
		final @AllowNull Long idEpJahre = _idEpJahre.get();

			if (idEpJahre == null) {
				addFehler(0, FEHLERTEXT);
				return false;
			}
		return true;
	}
}
