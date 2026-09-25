package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IFA01: Prüft, ob Förderschwerpunkt des Schülers korrekt ist.
 */
public final class ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKatalog;

	/**
	 * @param idKatalog	IdKatalog
	 * @param kontext	Kontext
	 */
	public ValidatorIjja01IntKatalogJahrgaengeJahrgangAsdKatalog(
			final @NotNull Supplier<@NotNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIjja02IntKatalogJahrgaengeJahrgangAsdKatalog(idKatalog, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Jahrgaenge jahrgang =
				Jahrgaenge.data().getWertByIDOrNull(_idKatalog.get());

		if (jahrgang == null) {
			addFehler(0, "Jahrgang ASD-Kürzel: Das Feld 'Jahrgang ASD-Kürzel' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
