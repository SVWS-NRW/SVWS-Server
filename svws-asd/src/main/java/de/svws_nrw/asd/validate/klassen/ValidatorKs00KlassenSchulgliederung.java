package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KS00: Prüft, ob für die Klasse eine Schulgliederung angegeben wurde.
 */
public final class ValidatorKs00KlassenSchulgliederung extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idSchulgliederung;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idSchulgliederung   SchulgliederungID
	 * @param kontext             der Kontext des Validators
	 */
	public ValidatorKs00KlassenSchulgliederung(
			final @NotNull Supplier<@AllowNull Long> idSchulgliederung,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idSchulgliederung = idSchulgliederung;

		_validatoren.add(new ValidatorKs01KlassenSchulgliederung(getNotNullSupplierLong(idSchulgliederung), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idSchulgliederung = _idSchulgliederung.get();

		if (idSchulgliederung == null) {
			addFehler(0, "Schulgliederung der Klasse: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
