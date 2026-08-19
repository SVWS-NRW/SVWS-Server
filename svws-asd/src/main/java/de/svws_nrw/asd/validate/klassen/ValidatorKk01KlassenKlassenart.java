package de.svws_nrw.asd.validate.klassen;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator KK01: Prüft, ob die angegebene Klassenart einer Klasse zulässig ist.
 */
public final class ValidatorKk01KlassenKlassenart extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKlassenart;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idKlassenart  KlassenartID
	 * @param kontext       der Kontext des Validators
	 */
	public ValidatorKk01KlassenKlassenart(
			final @NotNull Supplier<@NotNull Long> idKlassenart,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKlassenart = idKlassenart;

		_validatoren.add(new ValidatorKk02KlassenKlassenart(idKlassenart, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Long idKlassenart = _idKlassenart.get();

		if (Klassenart.data().getSchluesselByIDOrNull(idKlassenart) == null) {
			addFehler(0, "Art der Klasse: Das Feld 'Klassenart' muss zulässig sein.");
			return false;
		}

		return true;

	}
}
