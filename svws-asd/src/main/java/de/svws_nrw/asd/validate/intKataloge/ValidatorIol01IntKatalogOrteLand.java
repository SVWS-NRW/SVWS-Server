package de.svws_nrw.asd.validate.intKataloge;

import java.util.function.Supplier;

import de.svws_nrw.asd.types.schule.Laender;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import jakarta.validation.constraints.NotNull;

/**
 * Validator IOL01: Prüft, ob Land korrekt ist.
 */
public final class ValidatorIol01IntKatalogOrteLand extends Validator {

	private final @NotNull Supplier<@NotNull Long> _idKatalog;

	/**
	 * @param idKatalog	IdKatalog
	 * @param kontext	Kontext
	 */
	public ValidatorIol01IntKatalogOrteLand(
			final @NotNull Supplier<@NotNull Long> idKatalog,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this._idKatalog = idKatalog;

		_validatoren.add(new ValidatorIol02IntKatalogOrteLand(idKatalog, kontext));
	}

	@Override
	protected boolean pruefe() {

		final Laender laender =
				Laender.data().getWertByIDOrNull(_idKatalog.get());

		if (laender == null) {
			addFehler(0, "Das Feld 'Land' muss zulässig sein.");
			return false;
		}

		return true;
	}

}
