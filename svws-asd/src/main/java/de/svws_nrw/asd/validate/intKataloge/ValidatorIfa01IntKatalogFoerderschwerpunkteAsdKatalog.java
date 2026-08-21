package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Foerderschwerpunkt;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IFA01: Prüft, ob Förderschwerpunkt des Schülers korrekt ist.
 */
public final class ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKatalog;

	/**
	 * @param idKatalog	IdKatalog
	 * @param kontext	Kontext
	 */
	public ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog(
			final @NotNull Supplier<@NotNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog(idKatalog, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Foerderschwerpunkt foerderschwerpunkt =
				Foerderschwerpunkt.data().getWertByIDOrNull(_idKatalog.get());

		if (foerderschwerpunkt == null) {
			addFehler(0, "Foerderschwerpunkt des Schülers: Das Feld 'Förderschwerpunkt ASD-Kürzel' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
