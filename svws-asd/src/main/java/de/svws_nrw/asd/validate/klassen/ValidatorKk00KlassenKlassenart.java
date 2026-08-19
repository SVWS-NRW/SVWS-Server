package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK00: Prüft, ob für die Klasse eine Klassenart angegeben wurde.
 */
public final class ValidatorKk00KlassenKlassenart extends Validator {

	private final @NotNull Supplier<@AllowNull Long> _idKlassenart;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart  KlassenartID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorKk00KlassenKlassenart(
			final @NotNull Supplier<@AllowNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;

		_validatoren.add(new ValidatorKk01KlassenKlassenart(getNotNullSupplierLong(idKlassenart), kontext));
	}

	@Override
	protected boolean pruefe() {
		final Long idKlassenart = _idKlassenart.get();

		if (idKlassenart == null) {
			addFehler(0, "Art der Klasse: Kein Wert vorhanden.");
			return false;
		}

		return true;
	}

}
